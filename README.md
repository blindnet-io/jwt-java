<h1 align="center">
  blindnet devkit<br />
  JWT library for Java
</h1>

<p align=center><img src="https://user-images.githubusercontent.com/7578400/163277439-edd00509-1d1b-4565-a0d3-49057ebeb92a.png#gh-light-mode-only" height="80" /></p>
<p align=center><img src="https://user-images.githubusercontent.com/7578400/163549893-117bbd70-b81a-47fd-8e1f-844911e48d68.png#gh-dark-mode-only" height="80" /></p>

<p align="center">
  <strong>Library for creating, parsing, signing and verifying blindnet JWTs in Java applications</strong>
</p>

<p align="center">
  <a href="https://blindnet.dev"><strong>blindnet.dev</strong></a>
</p>

<p align="center">
  <a href="https://blindnet.dev/docs">Documentation</a>
  &nbsp;•&nbsp;
  <a href="https://github.com/blindnet-io/jwt-java/issues">Submit an Issue</a>
  &nbsp;•&nbsp;
  <a href="https://join.slack.com/t/blindnet/shared_invite/zt-1arqlhqt3-A8dPYXLbrnqz1ZKsz6ItOg">Online Chat</a>
  <br>
  <br>
</p>

## Get Started

:rocket: Check out our [Quick Start Guide](https://blindnet.dev/docs/quickstart) to get started in a snap.

## Installation

Add the library to your project's dependencies:

### Maven

```xml
<repository>
    <id>blindnet-snapshots</id>
    <url>https://nexus.blindnet.io/repository/maven-snapshots</url>
</repository>

<dependency>
    <groupId>io.blindnet</groupId>
    <artifactId>jwt-java</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Gradle

```kotlin
implementation("io.blindnet:jwt-java:1.0-SNAPSHOT")
```

## Usage

<!--📑 The API reference of {type of project, e.g. this SDK} is available on [blindnet.dev](https://blindnet.dev/docs/api_reference/[path-to-project}/latest).-->

```java
// Replace APP_ID and APP_KEY with your app values
TokenBuilder builder = new TokenBuilder(APP_ID, TokenPrivateKey.fromString(APP_KEY));

// Application tokens are used to authenticate your application against devkit components
String appToken = builder.app();

// User tokens authenticate registered users
String userToken = builder.user("user_id");

// Anonymous tokens can be used to allow an unknown user to perform some actions,
// like creating some privacy requests
String anonToken = builder.anonymous();
```

For a more complete example, the [storage-connector-java](https://github.com/blindnet-io/storage-connector-java) repo
contains a demo application implementing authentication of users and handling of privacy requests.

## Contributing

Contributions of all kinds are always welcome!

If you see a bug or room for improvement in this project in particular, please [open an issue][new-issue] or directly [fork this repository][fork] to submit a Pull Request.

If you have any broader questions or suggestions, just open a simple informal [DevRel Request][request], and we'll make sure to quickly find the best solution for you.

## Community

> All community participation is subject to blindnet’s [Code of Conduct][coc].

Stay up to date with new releases and projects, learn more about how to protect your privacy and that of our users, and share projects and feedback with our team.

- [Join our Slack Workspace][chat] to chat with the blindnet community and team
- Follow us on [Twitter][twitter] to stay up to date with the latest news
- Check out our [Openness Framework][openness] and [Product Management][product] on Github to see how we operate and give us feedback.

## License

The blindnet devkit jwt-java library is available under [MIT][license] (and [here](https://github.com/blindnet-io/openness-framework/blob/main/docs/decision-records/DR-0001-oss-license.md) is why).

<!-- project's URLs -->
[new-issue]: https://github.com/blindnet-io/jwt-java/issues/new/choose
[fork]: https://github.com/blindnet-io/jwt-java/fork

<!-- common URLs -->
[devkit]: https://github.com/blindnet-io/blindnet.dev
[openness]: https://github.com/blindnet-io/openness-framework
[product]: https://github.com/blindnet-io/product-management
[request]: https://github.com/blindnet-io/devrel-management/issues/new?assignees=noelmace&labels=request%2Ctriage&template=request.yml&title=%5BRequest%5D%3A+
[chat]: https://join.slack.com/t/blindnet/shared_invite/zt-1arqlhqt3-A8dPYXLbrnqz1ZKsz6ItOg
[twitter]: https://twitter.com/blindnet_io
[docs]: https://blindnet.dev/docs
[changelog]: CHANGELOG.md
[license]: LICENSE
[coc]: https://github.com/blindnet-io/openness-framework/blob/main/CODE_OF_CONDUCT.md
