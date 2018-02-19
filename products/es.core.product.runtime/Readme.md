
# Elexis-Server

The Elexis-Server is a headless variant of the Elexis desktop application. It is an entire rewrite, based on Equinox and an alternative persistence implementation based on EclipseLink.

Current snapshot binaries are available at [download](http://download.elexis.info/elexis-server/master/products/).

## Configuration

While Elexis-Server is can be operated on both Windows, MacOS and Linux, development is heavily focused on usage within the Linux operating system. We additionally focus on operation using Debian 8.

### Required files

For correct configuration the following files are required to be located in `${user.home}/elexis-server`. The directory `elexis-server` will be created on start if not available.

*   `shiro.ini`
*   `elexis-connection.xml` 

You may just copy the bundles/es.core/shiro.ini and bundles/es.core.connector.elexis/rsc/devel/elexis-connection.xml to ~/elexis-server and modify them there.

## Operation

In order to start up elexis-server the enclosed shell script `linux-start.sh` should be used. This creates a local telnet socket on port 7234 bound to the OSGI console.

On Windows use the provided script `win-start.bat`. To install telnet client to access the OSGi console see [Microsoft help](https://technet.microsoft.com/en-us/library/cc771275.aspx).

Be aware that the used Elexis database must contain a row with the param "locale" and a value corresponding to your system locale in the table config. Or you will see in the ~/elexis-server/logs/elexis-server.ERROR.log
a line like

`20:26:59.350 ERROR i.e.s.c.c.e.i.ElexisEntityManager - System locale [de_CH] does not match required database locale [null]`

After you should be able to see the uptime of the elexis server, if you visit with your browser http://localhost:8380/services. It should report something like `Uptime: 0 days, 0 hours, 0 min, 19 sec`

Now you must install the FHIR REST feature by opening a telnet console via `telnet localhost 7234` and calling `es_p2 features install info.elexis.server.fhir.rest.core.feature.feature.group`
Now it should be possible to visit with your browser URLs like http://localhost:8380/fhir/Patient?name=TestPatient, which should start a window containing something like

```
This result is being rendered in HTML for easy viewing. You may access this content as Raw JSON or Raw XML, or view this content in HTML JSON or HTML XML.
<Bundle xmlns="http://hl7.org/fhir">
   <id value="9939c948-2977-4512-b53d-074e4be9276c"></id>
   <meta>
      <lastUpdated value="2016-12-20T18:25:34.852+01:00"></lastUpdated>
   </meta>
   <type value="searchset"></type>
   <total value="2"></total>
   <link>
      <relation value="self"></relation>
      <url value="http://localhost:8380/fhir/Patient?name=TestPatient"></url>
   </link>
   <entry>
      <fullUrl value="http://localhost:8380/fhir/Patient/s9b71824bf6b877701111"></fullUrl>
      <resource>
         <Patient xmlns="http://hl7.org/fhir">
            <id value="s9b71824bf6b877701111"></id>
            <identifier>
               <system value="www.elexis.info/objid"></system>
               <value value="s9b71824bf6b877701111"></value>
            </identifier>
            <identifier>
               <system value="www.elexis.info/patnr"></system>
               <value value="2"></value>
            </identifier>
            <name>
               <use value="official"></use>
               <family value="Testpatientin"></family>
               <given value="Vorname"></given>
            </name>
            <gender value="female"></gender>
            <birthDate value="1988-06-23"></birthDate>
            <address>
               <line value="Teststrasse 22c"></line>
               <city value="Testhausen"></city>
               <postalCode value="68223"></postalCode>
            </address>
         </Patient>
      </resource>
   </entry>
```

Or in case of a non existing patient
```
This result is being rendered in HTML for easy viewing. You may access this content as Raw JSON or Raw XML, or view this content in HTML JSON or HTML XML.

<Bundle xmlns="http://hl7.org/fhir">
   <id value="1b44dfec-9a04-4c5f-ac71-2f8ccb055311"></id>
   <meta>
      <lastUpdated value="2016-12-20T18:30:59.949+01:00"></lastUpdated>
   </meta>
   <type value="searchset"></type>
   <total value="0"></total>
   <link>
      <relation value="self"></relation>
      <url value="http://localhost:8380/fhir/Patient?name=TestPatientxx"></url>
   </link>
</Bundle>
```

## Console commands

Elexis-server bundles partially provide console commands to be executed:


* Core `es (status | launch | system)` 
* P2 `es_p2  (system | repositories | features)` 

Example installing the FHIR REST feature:

* `es_p2 executeUpdate` update with the newest versions from the p2 site
* `es_p2 features install info.elexis.server.fhir.rest.core.feature.feature.group` install the feature
* `es system halt` halt the running elexis server, restart using a start script
