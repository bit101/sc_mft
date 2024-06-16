(
MFT.init;
~playing = false;
~freq = 440;

// knob 0 controls frequency on twist. init to freq value.
MFT.set(0, ~freq.explin(200, 1000, 0.1, 127));
MFT.twist(\freq, 0, { 
  |val|
  ~freq = val.linexp(0.1, 127, 200, 1000);
  if(~playing, { f.set(\freq, ~freq)});
});

// knob 15 is play/stop on press. strobes when playing.
MFT.down(\play, 15, {
  ~playing = ~playing.not;
  MFT.strobe(15, ~playing);
  if(~playing, {
    f = x.play(args: [\freq, ~freq]);
  }, {
    f.release;
  });
});

// simple osc
x = {
  | freq = 440 |
  SinOsc.ar(freq) ! 2;
};
)

