# Contributing to StackState SAP StackPack

First of all, thanks for contributing!

This document provides some basic guidelines for contributing to this repository.
To propose improvements, feel free to submit a PR.

## Submitting issues

  * If you think you've found an issue, please contact our [support][support].
  * Alternatively, you can open a Github issue.

## Pull Requests

Have you fixed a bug or written a new check and want to share it? Many thanks!

In order to ease/speed up our review, here are some items you can check/improve
when submitting your PR:

  * have a [proper commit history](#commits) (we advise you to rebase if needed).
  * summarize your PR with an explanatory title and a message describing your
    changes, cross-referencing any related bugs/PRs.
  * open your PR against the `master` branch.

### Keep it small, focused

Avoid changing too many things at once. For instance if you're fixing the NTP
check and at the same time shipping a dogstatsd improvement, it makes reviewing
harder and the _time-to-release_ longer.

### Commit Messages

Please don't be this person: `git commit -m "Fixed stuff"`. Take a moment to
write meaningful commit messages.

The commit message should describe the reason for the change and give extra details
that will allow someone later on to understand in 5 seconds the thing you've been
working on for a day.

### Squash your commits

Please rebase your changes on `master` and squash your commits whenever possible,
it keeps history cleaner and it's easier to revert things. It also makes developers
happier!

[support]: https://support.stackstate.com
