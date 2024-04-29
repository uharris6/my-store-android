# My Store App

## Steps
- Clone the repo
- Sync with gradle
- Run the app

The app was created with AGP 8.3.2 and Gradle 8.4 also it's using java 17

To run the test use the gradle task ./gradlew testDebugUnitTest

## Architecture
To implement the app it was decided to use Clean Architecture with MVI to benefit of the use of coroutines and flows
on the data layer and ViewModel in the UI layer because of the association of the ViewModel with the
lifecycle and the compose components.

## Design Patterns
The most important design patterns use in the implementation of the app are:
1. Singleton, with the creation of Retrofit as a Singleton we ensure that is has only one instance to be use
in the different services.
2. Observer, this is the way we communicate between the ViewModel and the Composable Component, in that way
the Component is just waiting that the State so it could implement an specific view.
3. State, Inside composable the way it works is using states, in that way every time the state change
it will start the process of recomposition of the components associated with that state.

## External Libs
Some external libraries that were use inside the app are:
1. Hilt with Dagger, create the dependency graph of the classes to be use inside the app.
2. Coil, image loader using URL.
3. Coroutines and Flow, background management of different request that could be need it inside the app.
Example: REST request, Database saving and retrieving, Big operational process.
4. Retrofit and GSON, Http Client that helps in the connection to Web Services and the deserialization of JSONs.
5. Compose, creation of components to create native UI for Android.
6. Android Navigation, navigation between compose components that allows to sent information between components.
7. Mockk, that helps to mock the different classes and object and to implements Unit tests.
8. Turbine, to test flows
