# Documentation

## Twist knob functions

### twist

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

---
### setNorm

`Mft.setNorm(ccNum, normalValue)`

Sets the value of a knob using a normalized value (0.0 to 1.0). The actual value of the knob reported may be different if a control spec has been defined.

---
### getNorm

`Mft.getNorm(ccNum)`

Returns the value of a knob normalized to a value from 0.0 to 1.0.

---
### set

`Mft.set(\symbol, value)`

Sets the value of a knob that has been set up with the `Mft.twist` function. It uses the symbol name and control spec for that knob.

---
### get

`Mft.get(\symbol)`

Gets the specced value of a knob that has been set up with the `Mft.twist` function.

---
### post

`Mft.post(\symbol)`

Posts the name and specced value of a knob that has been set up with the `Mft.twist` function.

---

## Knob button functions

### click

`Mft.click(\symbol, ccNum, function)`

Sets a function that will execute when a knob is clicked. The function will be passed 127 on press and 0 on release.

Example:

```
Mft.click(\clicked, 0, {
    |val|
    postln(val);
});
```

---
### press

`Mft.press(\symbol, ccNum, function)`

Sets a function that will execute when a knob is pressed. It's the same as using `Mft.click` and checking that the value is 127 before doing something.

Example:

```
Mft.press(\pressed, 0, {
    postln("pressed");
});
```

---
### release

`Mft.release(\symbol, ccNum, function)`

Sets a function that will execute when a knob is released. It's the same as using `Mft.click` and checking that the value is 0 before doing something.

Example:

```
Mft.release(\released, 0, {
    postln("released");
});
```

---
### toggle

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

---

## Side button functions

### sideButton

`Mft.sideButton(\symbol, buttonNum, bankNum, function)`

Sets a function that will execute when a given button (1-6) in a given bank (1-4) is clicked. Note: buttons 2 and 5 on each banck are set to change the bank, so they will not respond to this method. The function will receive a value of 127 on press and 0 on release.

---
### sideButtonPress

`Mft.sideButtonPress(\symbol, buttonNum, bankNum, function)`

Same as `Mft.sideButton` but only responds to presses. No value is passed to the function.

---
### sideButtonRelease

`Mft.sideButtonRelease(\symbol, buttonNum, bankNum, function)`

Same as `Mft.sideButton` but only responds to releases. No value is passed to the function.

---
### sideButtonToggle

`Mft.sideButtonToggle(\symbol, buttonNum, bankNum, onFunc, offFunc)`

Alternately calls `onFunc` and `offFunc` each time a side button is pressed.

---

## LED control

### cycleRGB

`Mft.cycleRGB(ccNum, on)`

Sets the given rgb led cycling through colors if `on` is true, or stops cycling if `on` is false.

---
### strobe

`Mft.strobe(ccNum, rate)`

Sets the given rgb led flashing. Rate: 0 = no strobe, 8 = fastest.

---
### pulse

`Mft.pulse(ccNum, rate)`

Sets the given rgb led pulsing. Rate: 0 = no strobe, 7 = fastest.

---
### strobeRing

`Mft.strobeRing(ccNum, rate)`

Sets the given ring leds flashing. Rate: 0 = no strobe, 8 = fastest.

---
### pulseRing

`Mft.pulseRing(ccNum, rate)`

Sets the given ring leds pulsing. Rate: 0 = no strobe, 8 = fastest.

---
### setRGB

`Mft.setRGB(ccNum, value)`

Sets the given rgb led's color. 0 = active color, 127 = inactive color.
Values 1-126 set colors on a spectrum made of blue, cyan, green, yellow, orange, red, magenta, purple.

---

## Custom control

### control

`Mft.control(channel, ccNum, value)`

Sends an arbitrary value to a cc on a channel. For implementing any features not covered above.
