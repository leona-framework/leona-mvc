# Leona Framework - MVC Libraries

Welcome to the **Leona Framework** repository, containing a set of libraries designed to enhance your Spring MVC applications by providing streamlined functionality and observability. This repository is divided into several modules, each serving a specific purpose to improve different aspects of your Spring MVC projects.

### Modules Overview

1. **leona-mvc-app-flow**:
    - This library automatically logs the entry and exit flow of traffic through an MVC application.
    - It offers configuration options for extractors to automatically extract header values into logging MDC (and propagate them elsewhere).

2. **leona-mvc-client**:
    - The standout feature is the **TypedRestClient** and **GenericRestClient**.
    - These clients are fully configurable through Spring's configuration, leveraging the application.yaml for easy setup.
    - Built-in filter classes allow for expandability and modularization of the client classes.
    - Additionally, this module provides a built-in retry mechanism using resilience4j.

3. **leona-mvc-components**:
    - This module houses various components utilized by other leona-mvc libraries.

4. **leona-mvc-observability**:
    - Offers observability enhancements, including metrics and metric-tracking for leona-mvc modules.
    - The mvc-services module gains tracking on the execution of functions via the default handling methods.

5. **leona-mvc-services**:
    - Provides two valuable interfaces, **SynchronousService** and **AsyncService**.
    - Inheriting classes benefit from a range of useful default methods that decorate functions, enabling automatic logging, timing, and manipulation.

6. **leona-mvc-testing**:
    - This module serves testing utilities for the leona-mvc libraries, helping you ensure the stability and correctness of your code.

### Getting Started

Each module in the Leona MVC Framework is designed to address specific challenges in Spring MVC application development. To begin using the libraries, simply explore the relevant module that aligns with your project's requirements. The well-organized structure and detailed documentation within each module will guide you through setup, configuration, and usage.

Feel free to contribute, report issues, or suggest improvements. We are excited to have you as part of the Leona Framework community!

For more information about each library and how to use them effectively, please refer to the respective documentation in each module.

Enjoy building better Spring MVC applications with the Leona Framework!

---

*Note: This repository is part of the Leona Framework, a collection of modules aimed at enhancing Spring MVC applications. Each module focuses on specific functionality to provide developers with efficient tools and practices for building robust and observable applications.*