Mft {
    classvar isSetup = false;
    classvar mftID;
    classvar toggles;
    classvar sideToggles;
    classvar ccValues;
    classvar symCCs;
    classvar specs;
    classvar <out;

    *init {
        | outPort = 0 |
        var mft;
        if (isSetup != true, {
            "Setting up Midi Fighter Twister. Please wait...".postln;
            MIDIClient.init(verbose: false);
            MIDIIn.connectAll(false);
            MIDIdef.freeAll;
            toggles = false ! 128;
            sideToggles = false ! 32;
            ccValues = 0 ! 128;
            symCCs = ();
            specs = nil ! 128;

            mft = MIDIClient.destinations.detect({|e| e.device == "Midi Fighter Twister"});
            if (mft != nil, {
                out = MIDIOut.new(outPort, mft.uid);
                if(thisProcess.platform.name == \linux, {
                    out.connect(mft.uid);
                }, {
                    out.connect();
                });
                isSetup = true;
                "Midi Fighter Twister is set up!".postln;
            }, {
                "Midi Fighter Twister not found.".postln;
            });
        }, {
            "Midi Fighter Twister is already set up!".postln;
        });
    }

    // Frees all MIDIdef functions, which are otherwise permanent.
    *freeAll {
      MIDIdef.freeAll;
    }

    /////////////////////////////////////
    // Twist knob values.
    /////////////////////////////////////

    // Sets a function to execute when a knob is twisted.
    // Takes an initial value and a control spec, so the value passed to
    // the function will always be in range.
    // Initializes the knob's value and display to the init value.
    *twist {
        | sym, cc = 0, initVal = 0, spec, func |
        var lastValue;
        symCCs[sym] = cc;
        specs[cc] = spec;
        Mft.setNorm(cc, spec.unmap(initVal));

        MIDIdef.cc(sym, {
            | val, num, chan, src |
            val = val / 127;
            ccValues[cc] = val;
            val = spec.map(val);
            if( val != lastValue, {
                lastValue = val;
                func.(val);
            });
        }, ccNum: cc, chan: 0).permanent_(true);
    }

    // Set the normalized value of a knob from 0.0 - 1.0 using its cc num.
    *setNorm {
        | cc = 0, val = 0 |
        val = val.clip(0, 1);
        ccValues[cc] = val;
        out.control(0, cc, val * 127);
    }

    // Gets the normalized value of a knob from 0.0 - 1.0 using its cc num.
    *getNorm {
        | cc |
        ^ccValues[cc];
    }

    // Sets the specced value of a knob using its symbol name (as set in the twist function). Will fail if symbol does not exist.
    *set {
        | sym, val |
        var cc;
        cc = symCCs[sym];
        Mft.setNorm(cc, specs[cc].unmap(val));
    }

    // Gets the specced value of a knob using its symbol name (as set in the twist function). Will fail if symbol does not exist.
    *get {
        | sym |
        var cc, val;
        cc = symCCs[sym];
        val = ccValues[cc];
        val = specs[cc].map(val);
        ^val;
    }

    // Posts the name and specced value of a knob (as set in the twist function). Will fail if symbol does not exist.
    *post {
        | sym |
        var val = Mft.get(sym);
        postf("%: %\n", sym, val);
    }

    /////////////////////////////////////
    // Knob buttons.
    /////////////////////////////////////

    // Set a function to execute when a knob is clicked.
    // It will get 127 on down, and 0 on up.
    *click {
        | sym, cc = 0, func |
        MIDIdef.cc(sym, {
            | val, num, chan, src |
            func.(val);
        }, ccNum: cc, chan: 1).permanent_(true);
    }

    // Set a function to execute when a knob is pushed down.
    *press {
        | sym, cc = 0, func |
        MIDIdef.cc(sym, {
            | val, num, chan, src |
            if(val == 127, func);
        }, ccNum: cc, chan: 1).permanent_(true);
    }

    // Set a function to execute when a knob is released.
    *release {
        | sym, cc = 0, func |
        MIDIdef.cc(sym, {
            | val, num, chan, src |
            if(val == 0, func);
        }, ccNum: cc, chan: 1).permanent_(true);
    }

    // Alternately calls one of two functions each time a knob is pressed.
    *toggle {
        | sym, cc = 0, on, off |
        MIDIdef.cc(sym, {
            | val, num, chan, src |
            if(val == 127, {
                toggles[cc] = toggles[cc].not;
                if(toggles[cc], on, off);
            });
        }, ccNum: cc, chan: 1).permanent_(true);
    }

    *clearToggles {
        (0..127).do({
            | i |
            toggles[i] = false;
        });
    }



    /////////////////////////////////////
    // Side buttons.
    /////////////////////////////////////

    // Set a function to execute when a side button is clicked.
    // It will get 127 on down, and 0 on up.
    // Button and bank numbers are 1-indexed here.
    // Buttons are 1-6, banks 1-4.
    // Note: by default, buttons 2 and 5 on each bank are set to change the bank,
    // so these will not respond to this method.
    *sideButton {
        | sym, num = 1, bank = 1, func |
        var cc;
        cc = 6 * bank + 1 + num;
        MIDIdef.cc(sym, {
            | val, num, chan, src |
            func.(val);
        }, ccNum: cc, chan: 3).permanent_(true);
    }

    // Set a function to execute when a side button is pushed down.
    // Button and bank numbers are 1-indexed here.
    // Buttons are 1-6, banks 1-4.
    // Note: by default, buttons 2 and 5 on each bank are set to change the bank,
    // so these will not respond to this method.
    *sideButtonPress {
        | sym, num = 1, bank = 1, func |
        var cc;
        cc = 6 * bank + 1 + num;
        MIDIdef.cc(sym, {
            | val, num, chan, src |
            if(val == 127, func);
        }, ccNum: cc, chan: 3).permanent_(true);
    }

    // Set a function to execute when a side button is released.
    // Button and bank numbers are 1-indexed here.
    // Buttons are 1-6, banks 1-4.
    // Note: by default, buttons 2 and 5 on each bank are set to change the bank,
    // so these will not respond to this method.
    *sideButtonRelease {
        | sym, num = 1, bank = 1, func |
        var cc;
        cc = 6 * bank + 1 + num;
        MIDIdef.cc(sym, {
            | val, num, chan, src |
            if(val == 0, func);
        }, ccNum: cc, chan: 3).permanent_(true);
    }

    // Alternately calls one of two functions each time a side button is pressed.
    // Button and bank numbers are 1-indexed here.
    // Buttons are 1-6, banks 1-4.
    // Note: by default, buttons 2 and 5 on each bank are set to change the bank,
    // so these will not respond to this method.
    *sideButtonToggle {
        | sym, num = 1, bank = 1, on, off |
        var cc;
        cc = 6 * bank + 1 + num;
        MIDIdef.cc(sym, {
            | val, num, chan, src |
            if(val == 127, {
                sideToggles[cc] = sideToggles[cc].not;
                if(sideToggles[cc], on, off);
            });
        }, ccNum: cc, chan: 3).permanent_(true);
    }


    /////////////////////////////////////
    // LED control.
    /////////////////////////////////////

    // Sets the rgb led cycling through colors.
    *cycleRGB {
        | cc=0, on=true |
        if(on, {
            out.control(5, cc, 127);
        }, {
            out.control(5, cc, 0);
        });
    }

    // Sets the rgb led flashing.
    // Rate = 0 (no strobe) to 8 (fast).
    *strobe {
        | cc=0, rate=4 |
        out.control(2, cc, rate.clip(0, 8));
    }

    // Sets the rgb led pulsing.
    // Rate = 0 (no pulse) to 7 (fast).
    *pulse {
        | cc=0, rate=4 |
        out.control(2, cc, 9 + rate.clip(0, 7));
    }

    // Sets the brightness of the rgb led.
    // Range: 0-30
    *rgbBrightness {
        | cc=0, bright=30 |
        out.control(2, cc, 17 + bright.clip(0, 30));
    }

    // Sets the ring leds flashing.
    // Rate = 0 (no strobe) to 8 (fast).
    *strobeRing {
        | cc=0, rate=4 |
        // 48 = off. 49-56 = on.
        out.control(5, cc, 48 + rate.clip(0, 8));
    }

    // Sets the ring leds pulsing.
    // Rate = 0 (no pulse) to 8 (fast).
    *pulseRing {
        | cc=0, rate=4 |
        rate = rate.clip(0, 8);
        if(rate == 0, {
            out.control(5, cc, 0);
        }, {
            // 57-64
            out.control(5, cc, 56 + rate);
        });
    }

    // Sets the color of the rgb led.
    // 0 = active color, 127 = inactive color.
    // 1-126 is a color on the spectrum:
    // blue, cyan, green, yellow, orange, red, magenta, purple.
    *setRGB {
        | cc = 0, val = 127 |
        out.control(1, cc, val);
    }



    /////////////////////////////////////
    // Custom control
    /////////////////////////////////////

    // Send a custom control message.
    *control {
        | chan = 0, cc=0, val = 0 |
        out.control(chan, cc, val);
    }
}


