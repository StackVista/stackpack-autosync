# StackState Custom Synchronization StackPack

This repository contains the StackState Custom Synchronization StackPack. The code is distributed under the [Apache 2.0](LICENSE.md) license.

## Introduction

This StackPack contains everything you need to automatically synchronize topology information from an external topology source.

## Documentation

The StackState [documentation](https://docs.stackstate.com/develop/creating_stackpacks/) describes how to build and install a StackPack.

## Building

The StackPack is built using the [SBT](https://www.scala-sbt.org/) tool.

In the main directory, compile the StackPack using:

```
sbt compile
```

## Testing

The StackPack comes with a small testing library that validates that the StackPack packaging is correct.

In the main directory, test the StackPack using:

```
sbt test
```

## Packaging

In the main directory, package the StackPack using:

```
sbt package
```

## Versioning

The StackPack release version is configured in the [version.sbt](version.sbt) file. StackPacks use [Semantic Versioning](https://semver.org/).

## Releasing

use `bin/publish.sh x.y.z`, ensure you are following semver

## Contributing

Contributions are welcome! Please check our [contribution instructions](CONTRIBUTING.md).

## Legal

Contributions to the Custom Synchronization StackPack are contributed under the "inbound=outbound" principle. This means
that contributions are licensed under the same license as the [source code](LICENSE.md).
We do not require you to sign a separate CLA (Contributor License Agreement) or any other kind of document.

Thank you for your contributions!
