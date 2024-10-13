# sc_mft

This is a simple extension for interacting with the [Midi Fighter Twister](https://www.midifighter.com/shop/midi-fighter-twister) device in [SuperCollider](https://supercollider.github.io/).

It gives you access to many of the features of the device with some simple constructs.

## Installing

Copy `MidiFighterTwister.sc` to your SuperCollider classes folder. You can find that by evaluating `Platform.userExtensionDir` in SC.

## Use

Evaluate `Mft.init` to set up the device in SuperCollider. When it's ready, all functionality is in class methods on `Mft`.

Functions are documented here: https://github.com/bit101/sc_mft/blob/main/documentation.md

## Caveats

- Tested on Linux and MacOS, not Windows.
- Expects a fairly default configuration of the device. If you've customized your MFT, some of these functions may not work as expected.

## Todo

- Switch banks.
- LED brightness.
