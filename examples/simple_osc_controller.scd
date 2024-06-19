(
Mft.init;
~freq = 440;
~amp = 0.2;
~pan = 0;
~playing = false;

// knobs 0, 1, 2 control frequency, ap, pan on twist. init to freq value.
Mft.set(0, ~freq.explin(20, 10000, 0.1, 127));
Mft.set(1, ~amp.explin(0.001, 1, 0.001, 127));
Mft.set(2, ~pan.linlin(-1, 1, 0.1, 127));

Mft.twist(\freq, 0, { 
  |val|
  ~freq = val.linexp(0.1, 127, 20, 10000);
  if(~playing, { f.set(\freq, ~freq)});
});

Mft.twist(\amp, 1, { 
  |val|
  ~amp = val.linexp(0.001, 127, 0.001, 1);
  if(~playing, { f.set(\amp, ~amp)});
});

Mft.twist(\pan, 2, { 
  |val|
  ~pan = val.linlin(0, 127, -1, 1);
  if(~playing, { f.set(\pan, ~pan)});
});

// knob 15 is play/stop on press. strobes when playing.
Mft.toggle(\play, 15, {
  Mft.strobe(15);
  ~playing = true;
  f = Synth(\simple, [\freq, ~freq, \amp, ~amp, \pan, ~pan]);
}, {
  Mft.strobe(15, 0);
  ~playing = false;
  f.free;
});

// simple osc
 SynthDef(\simple, {
  | freq = 440, amp = 0.2, pan = 0 |
  var sig;

  sig = SinOsc.ar(freq);
  sig = sig * amp;
  sig = Pan2.ar(sig, pan);
  Out.ar(0, sig);
}).add;
)

