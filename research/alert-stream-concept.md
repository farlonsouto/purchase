## Core Concept

In time-domain astronomy, astronomical transient alert streams are continuous, real-time data flows that notify the
scientific community of sudden changes in the sky, such as the appearance of a new source or a significant change in an
existing one. [1, 2, 3, 4, 5]

## Core Components

These streams typically consist of alert packets, which are standardized digital messages containing critical metadata
about a detection: [1, 6, 7, 8]

* **Identification:** Who detected the event and when.
* **Location:** Precise celestial coordinates (RA/Dec).
* **Measurement:** Photometric data showing the change in brightness or position.
* **Context:** Images of the source (postage-stamp "cutouts") and potential cross-matches with existing star
  catalogs. [6, 7, 9, 10]

## How the Streams Work

The process of managing these massive data volumes is often referred to as the Alert Ecosystem.

### References

1. [Alerts and brokers | Rubin Observatory][1]
2. [An overview of astronomical transient brokers in Rubin era][2]
3. [About - Fink broker][3]
4. [Fink broker welcome and architecture][4]
5. [AAS2RTO: Automated Alert Streams to Real-Time Observations][5]
6. [Fink white paper - science modules][6]
7. [Alert Classification for the ALeRCE Broker System][7]
8. [ALeRCE light curve classifier: Tidal disruption event expansion pack][8]
9. [The ZTF Alert Stream: Lessons from Operating an LSST Precursor][9]
10. [ALeRCE light curve classifier version 1][10]

[1]: https://rubinobservatory.org

[2]: https://astro.sk

[3]: https://fink-broker.org

[4]: https://fink-broker.org

[5]: https://aanda.org

[6]: https://fink-broker.org

[7]: https://caltech.edu

[8]: https://aanda.org

[9]: https://harvard.edu

[10]: https://iop.org
