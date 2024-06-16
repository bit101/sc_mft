Mft {
  classvar isSetup = false;
  classvar mftID;
  classvar <out;

  *init {
    var mft;
    if (isSetup != true, {
      "Setting up Midi Fighter Twister. Please wait...".postln;
      MIDIClient.init(verbose: false);
      MIDIIn.connectAll(false);
      MIDIdef.freeAll;

      mft = MIDIClient.sources.detect({|e| e.device == "Midi Fighter Twister"});
      if (mft != nil, { 
        mftID = mft.uid;
        out = MIDIOut(0);
        out.connect(mftID);
        isSetup = true;
        "Midi Fighter Twister is set up!".postln;
      });
    }, {
      "Midi Fighter Twister is already set up!".postln;
    });
  }

  // Set a function to execute when a knob is twisted.
  *twist {
    | sym, cc = 0, func |
    MIDIdef.cc(sym, {
      | val, num, chan, src |
        func.(val);
      }, ccNum: cc, chan: 0
    );
  }

  // Set a function to execute when a knob is clicked.
  // It will get 127 on down, and 0 on up.
  *click {
    | sym, cc = 0, func |
    MIDIdef.cc(sym, {
      | val, num, chan, src |
        func.(val);
      }, ccNum: cc, chan: 1
    );
  }

  // Set a function to execute when a knob is pushed down.
  *down {
    | sym, cc = 0, func |
    MIDIdef.cc(sym, {
      | val, num, chan, src |
      if(val == 127, { func.(val)});
      }, ccNum: cc, chan: 1
    );
  }

  // Set a function to execute when a knob is released.
  *up {
    | sym, cc = 0, func |
    MIDIdef.cc(sym, {
      | val, num, chan, src |
      if(val == 0, { func.(val)});
      }, ccNum: cc, chan: 1
    );
  }

  // Set the value of a knob.
  *set {
    | cc=0, val=0 |
    out.control(0, cc, val);
  }

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
  *strobe {
    | cc=0, on=true |
    if(on, {
      out.control(2, cc, 4);
    }, {
      out.control(2, cc, 0);
    });
  }

  // Sets the ring leds flashing.
  *strobeRing {
    | cc=0, on=true |
    if(on, {
      out.control(5, cc, 52);
    }, {
      out.control(5, cc, 0);
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

  // Send a custom control message.
  *control {
    | chan = 0, cc=0, val = 0 |
    out.control(chan, cc, val);
  }

}
