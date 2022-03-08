# BenchStress
BenchStress is a Java test suite designed to verify the accuracy of software vulnerability detection tools on the Insecure Deserialization vulnerability. It is an open source web application written in Java, that can be analyzed by any Static Application Security Testing (SAST) tool that supports Java and, in particular, the Insecure Deserialization vulnerability.
The intent is that all the vulnerabilities deliberately included in BenchStress are actually exploitable, so it is a fair test for any kind of application vulnerability detection tool.
BenchStress is focused on the unsafe Java object deserialization that belongs to the OWASP Top 10 list of the 2017, which is the list of the ten most critical web application security risks.
The deserialization has gain a lot of attention in the last years because of its high impact and severity, in fact it can have very dangerous effect. In Java, it might lead to remote code execution (RCE) or denial of service (DOS) attacks.

The goal of this project is to create a de facto benchmark application to evaluate the effectiveness of static security scanners, with a particular focus on Insecure Deserialization vulnerabilities.
This benchmark application contains portions of code that static security scanners cannot easily navigate, thus possibly hiding the included Insecure Deserialization vulnerabilities.

To execute a scan with your desired security scanner it is enough to download this repository (git clone https://github.com/alex97saba/BenchStress.git) and launch a scan on it through an IDE or from command line with your tool.