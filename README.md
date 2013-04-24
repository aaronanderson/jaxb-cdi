## Running the Application

mvn clean install jetty:run

access the application at http://localhost:8080/jaxb-cdi

## About

[jaxb-cdi]  is a contrived sample application to demonstrate contorting JAXB to leverage objects (beans) managed by CDI. A technique that this project presents is to define a domain specific application model in XML, use JAXB to fabricate a Java object model, and then use CDI to wire the Java model together. While the trend in JavaEE is to move away from XML this fabrication technique allows the melding of execution model and logic in a domain specific fashion which may be compelling in certain scenarios. It is worth noting that CDI Normal scopes expect one instance of a bean signature per scope which would limit the usefulness of processing an XML document with repetitive elements but CDI supports custom psudo-scopes where this constraint does not apply.

JAXB currently locks away object creation in inside ObjectFactories that rely on no-argument constructors. Because of this it is challenging to introduce CDI managed beans, specifically ones in a custom scope. CDI 1.1 introduced a means to obtain the CDI provider in a standard and static way and by extension exposing the BeanManager and CDI extension instance so this mitigates some of the problem. An ObjectFactory class can be passed into the JAXBContext and once instantiated can access the BeanManager and Extension to perform the CDI object manipulations.Still, as this code demonstrates, there is room for improved integration such as the following:

1)  XJC or XJC plugin enhancement to generate ObjectFactories that create the new instances through the BeanManager using the Class object.

2) JAXB Unmarshaller enhancement to pass in ObjectFactory instances at unmarshall time, similar to how XMLAdapter instances can be swapped in

3) Delaying the PostConstruct and PreDestroy JSR250 annotated method processing until after unmarshalling

4) Possibly introducing a lifecycle for the JAXB Binder interface so that both the JAXB CDI object and DOM models could be cleanup at the same time

Project Overview:

1) APIs:  JSF 2.0, JAXB 2.1 (JDK version), CDI 2.0, Jetty 8(for testing)

2) The crux of the application is to use a centrally managed UI configuration file to tailor the presentation of common framework pages based on the requested banner. This could be useful in a Cloud provider multi-tenent environment.

3) The WEB-INF/xsd/ui-config.xsd defines a UI branding model with elements for a single global setting configuration and then separate distinct brand configurations to tailor the look and feel of the site based on the requested company.

4) The WEB-INF/ui-config.xml is a realization of the model for this specific application, defining banners for company A and company B

5) CDIExtension is the CDI portal extension class, exposing the managed bean classes and defining the scope.

6) BannerUIContext and GlobalUIContext extend the common Context class which defines a container for scoped instances in the respective scopes. These classes act as a bridge between the application and CDI when setting the current scope

7) The BannerUI and GlobalUI classes extend the XJC generated equivalents to specify the scope the objects belong in. Custom execution logic could be added here and it's behaviour could be dictated based on the information defined in the XML. For this demonstration the XML information is utilized in JSF

8) The UIFilter class examines the incoming request and then sets the custom CDI scopes accordingly

9) The JSF application uses the custom scoped CDI managed beans to alter the presentation of the page based on the ui-config model


See [EclipseLink MOXy enhancement request](https://bugs.eclipse.org/bugs/show_bug.cgi?id=406032)    
