## Custom Synchronization

This StackPack contains everything you need to automatically synchronize topology information from an external topology source.

**Use the Custom Synchronization StackPack if you want to connect an external topology source that is not listed as a standard StackPack.**

## What is a synchronization?

StackState imports and consolidates topology information from various sources to create a real-time, up-to-date map of your IT landscape. A data pipeline processes incoming data and turns it into the topology. This data pipeline is called a _synchronization_. Check the StackState documentation site for additional details about [synchronizations](https://l.stackstate.com/FJggzk).

## What is a _custom_ synchronization?

StackState includes StackPacks with standard synchronizations for particular data sources, such as AWS, Azure, or the StackState agent. These StackPacks contain completely configured synchronizations that work with a particular data source.

If you want to connect an external data source that is not supported out of the box, a _custom_ synchronization is needed. This StackPack contains a synchronization that expects a [well-defined JSON input format](https://l.stackstate.com/oyJfJn) and turns this into topology in StackState.

## Creating a custom synchronization

The following steps are required to create a custom synchronization:

1. Install the Custom Synchronization StackPack (this StackPack)
2. Test the installed synchronization (instructions provided after the StackPack is installed)
3. Configure the external data source to push information to StackState using the [well-defined JSON input format](https://l.stackstate.com/oyJfJn)
