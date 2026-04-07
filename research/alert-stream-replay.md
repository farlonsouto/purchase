## A Kafka-Based Temporal Replay Architecture for Reproducible ML Training on Astronomical Transient Alert Streams

---

### Background & Motivation

Modern sky surveys generate staggering continuous data volumes. The Zwicky Transient Facility (ZTF) images the entire
Northern Sky every three nights, detecting about 1,000 objects in each image taken every 45 seconds — roughly 1 million
changing objects per night. The data pipeline characterizes these objects and broadcasts alert packets for downstream
analysis. ZTF chose Kafka + Avro as its distribution backbone for exactly this reason.

NASA's General Coordinates Network (GCN) allows real-time alerts in the astronomy community, where observatories publish
alerts when they witness transient phenomena — black holes, neutron star mergers, gamma-ray bursts — and anyone can
subscribe to those real-time alerts.

The upcoming Vera C. Rubin Observatory (LSST) will dwarf even ZTF. Seven "full-stream" brokers — ALeRCE, AMPEL, ANTARES,
Fink, Lasair, and others — will receive and process the full LSST alert stream, performing filtering, cross-matching,
photometric classification, and ML-driven prioritization for follow-up observations.

This is the real operational context. Your project fits cleanly into it.

---

### The Core Problem You Solve

There is an important gap between real-time alert streaming and reproducible science. ML pipelines have unique
versioning requirements: training data must be frozen at specific points in time to ensure reproducibility. "
Point-in-time correct" means capturing feature values exactly as they existed at a specific timestamp, avoiding data
leakage where future information accidentally influences historical training data.

In an astronomical context this is acute: a classifier trained on ZTF alerts from, say, 2021–2022 behaves differently
from one trained on 2023 data (instrument calibration drift, new object populations, schema updates). Reproducing that
exact classifier — or challenging its results — requires replaying the stream as it existed at that time window, not a
static archive dump.

Without versioning, you cannot reproduce the training dataset that was used three months ago, making model debugging
impossible.

---

### Technical Foundation

Kafka makes this tractable at a low level. Kafka lets consumers seek to offsets corresponding to timestamps. The pattern
is: convert the target timestamp to milliseconds since epoch, ask the broker for the earliest offset whose record
timestamp is ≥ that timestamp for each partition, then seek the consumer to those offsets and begin consuming. Your Java
sandbox is already the right vehicle for implementing this `offsetsForTimes` API.

By adjusting consumer offsets, you can control which messages to replay. Strategies include offset-based replay (
resetting consumer offsets), mirror topics (a duplicate topic for archival replay independent of the main topic), and
custom replay services implementing any custom replay logic.

---

### Concrete Research Scope (3 tightly defined contributions)

**1. Temporal window consumer** — A Java consumer using `offsetsForTimes()` that accepts `[T_start, T_end]` and
reconstructs an exact bounded slice of an astronomical alert stream. The key research question is how to handle
multi-partition timestamp skew and maintain event-time ordering across partitions (not trivial with ZTF's multi-sensor
setup).

**2. Schema-versioned replay with Avro** — ZTF/LSST alert schemas evolve. Avro, an open-source JSON-based binary format,
was chosen for serializing alert messages because it is compact and easy to characterize with simple JSON schemas. Your
contribution is handling replay across schema versions — a consumer that can deserialize alerts from any historical
window regardless of schema drift, using a local Schema Registry.

**3. Reproducibility audit log** — A secondary Kafka topic that records each replay session's metadata:
`(topic, T_start, T_end, partition offsets resolved, schema versions encountered, record count)`. This becomes a
verifiable citation for a paper: "model M was trained on stream window W, provably reproducible by replaying offsets
O₁..O₂."

---

### Data Source

You can build this against **NASA's public GCN Kafka stream** (`kafka.gcn.nasa.gov`) — it's openly accessible, actively
producing real gamma-ray burst and supernova alerts, and already used in published research. Unlike most Kafka demos
which rely on simulated data, NASA's publicly accessible Gamma-ray Coordinates Network integrates data from supernovas
and black holes coming from various satellites and is available as a live Kafka stream. This gives your project real
scientific credibility without needing telescope access.

---

### Literature Foundation

These are the key references to anchor your proposal:

| Source                                                                                        | Why it matters                                                                                                   |
|-----------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|
| Patterson et al. (2019) *"Streaming Data from the Universe with Apache Kafka"*, Confluent/ZTF | Direct precedent: ZTF's production Kafka architecture                                                            |
| Bellm et al. (2019), *Publications of the Astronomical Society of the Pacific*                | The ZTF alert system peer-reviewed paper (`doi:10.1088/1538-3873/ab0c2a`)                                        |
| Chaves et al. (2024), *Expert Systems* `doi:10.1111/exsy.13287`                               | Kafka-ML: management of ML/AI pipelines through data streams, including support for PyTorch and GPU acceleration |
| Conduktor (2024), *"Data Versioning in Streaming"*                                            | Conceptual framework for point-in-time correctness                                                               |
| NASA GCN at Current 2023, Confluent                                                           | Kafka for open science, OAuth2 scaling to public access                                                          |
| Rubin Observatory broker documentation                                                        | Forward-looking scope: your architecture as infrastructure for LSST-scale brokers                                |

---

### Technical viability and scope

It's bounded (one domain, one data source, three clearly separated system components), it's grounded in active
production systems rather than toy examples, the reproducibility angle is genuinely under-addressed in the astronomy
broker ecosystem. This Kafka sandbox maps directly onto the implementation — `KafkaConsumer.offsetsForTimes()`, Avro
deserialization, and producer-side audit topics are all well within reach with our goals.