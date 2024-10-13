# sc_mft

This is a simple class for intercting with the [Midi Fighter Twister](https://www.midifighter.com/shop/midi-fighter-twister) device in [SuperCollider](https://supercollider.github.io/).

It gives you access to many of the features of the device with some simple constructs.

## Installing

Copy `MidiFighterTwister.sc` to your SuperCollider classes folder. You can find that by evaluating `Platform.userExtensionDir` in SC.

## Use

Evaluate `Mft.init` to set up the device in SuperCollider. When it's ready, all functionality is in class methods on `Mft`.

### Twist knob functions

#### twist

`Mft.twist(\symbol, ccNum, initialValue, controlSpec, function)`

Sets a function to run when the knob is turned. This function will get the value of the knob as defined by the control spec.

Example:

```
Mft.twist(\freq, 0, 440, ControlSpec(20, 10000, \exp), {
    |val|
    Mft.post(\freq);
    // set the frequency of a synth perhaps.
});
```

#### setNorm

`Mft.setNorm(ccNum, normalValue)`

Sets the value of a knob using a normalized value (0.0 to 1.0). The actual value of the knob reported may be different if a control spec has been defined.

#### getNorm

`Mft.getNorm(ccNum)`

Returns the value of a knob normalized to a value from 0.0 to 1.0.

#### set

`Mft.set(\symbol, value)`

Sets the value of a knob that has been set up with the `Mft.twist` function. It uses the symbol name and control spec for that knob.

#### get

`Mft.get(\symbol)`

Gets the specced value of a knob that has been set up with the `Mft.twist` function.

#### post

`Mft.post(\symbol)`

Posts the name and specced value of a knob that has been set up with the `Mft.twist` function.

### Knob button functions

#### click

`Mft.click(\symbol, ccNum, function)`

Sets a function that will execute when a knob is clicked. The function will be passed 127 on press and 0 on release.

Example:

```
Mft.click(\clicked, 0, {
    |val|
    postln(val);
});
```

#### press

`Mft.press(\symbol, ccNum, function)`

Sets a function that will execute when a knob is pressed. It's the same as using `Mft.click` and checking that the value is 127 before doing something.

Example:

```
Mft.press(\pressed, 0, {
    postln("pressed");
});
```

#### release

`Mft.release(\symbol, ccNum, function)`

Sets a function that will execute when a knob is released. It's the same as using `Mft.click` and checking that the value is 0 before doing something.

Example:

```
Mft.release(\released, 0, {
    postln("released");
});
```

#### toggle

`Mft.toggle(\symbol, ccNum, onFunction, offFunction)`

Sets two functions that will run alternately each time the button is clicked.

Example:

```
Mft.toggle(\toggler, 0, {
    postln("it's on!");
}, {
    postln("it's off!");
});
```

### Side button functions

Documentation coming soon.

### LED control

Documentation coming soon.

### Custom control

Documentation coming soon.



## Caveats

- Tested on Linux and MacOS, not Windows.
- Expects a fairly default configuration of the device. If you've customized your MFT, some of these functions may not work as expected.

## Todo

- Switch banks.
- LED brightness.
