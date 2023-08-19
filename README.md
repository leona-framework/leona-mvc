# Leona Framework - MVC Libraries

Welcome to the **Leona Framework** repository. Here, you'll find a set of libraries designed to enhance your Spring MVC applications by streamlining functionality and improving observability. The repository is divided into distinct modules, each serving a specific purpose to enhance various aspects of your Spring MVC projects.

### Overview of Modules

1. **leona-mvc-app-flow**:
   - This library automatically logs the flow of traffic into and out of an MVC application.
   - It provides options to configure extractors that automatically pull header values into the logging MDC (and can propagate them to other places).

2. **leona-mvc-client**:
   - The highlight of this module is the **TypedRestClient** and **GenericRestClient**.
   - These clients are fully customizable through Spring's configuration, making use of the application.yaml for easy setup.
   - Internal filter classes enable the expansion and modularization of the client classes.
   - Additionally, the module includes a built-in retry mechanism using resilience4j.

3. **leona-mvc-components**:
   - This module contains various components used by other leona-mvc libraries.

4. **leona-mvc-observability**:
   - Offers observability enhancements, including metrics and metric tracking for leona-mvc modules.
   - The mvc-services module gains the ability to track function execution through the default handling methods.

5. **leona-mvc-services**:
   - Provides two valuable interfaces, **SynchronousService** and **AsyncService**.
   - Inherited classes benefit from a range of helpful default methods that decorate functions, enabling automatic logging, timing, and manipulation.

6. **leona-mvc-testing**:
   - This module offers testing utilities for the leona-mvc libraries, helping you ensure the stability and correctness of your code.

### Getting Started

Each module within the Leona MVC Framework addresses specific challenges in Spring MVC application development. To start using the libraries, explore the relevant module that aligns with your project's needs. Clear documentation within each module will guide you through setup, configuration, and usage.

You're welcome to contribute, report issues, or suggest improvements. We're excited to have you as part of the Leona Framework community!

For comprehensive information about each library and how to use them effectively, refer to the dedicated documentation in each module.

Enjoy building more robust Spring MVC applications with the Leona Framework!

---

*Note: This repository is a component of the Leona Framework, a collection of modules designed to enhance Spring MVC applications. Each module focuses on specific functionality, providing developers with efficient tools and practices for building sturdy and observable applications.*