(
Mft.init;

// knobs 0, 1, 2 control frequency, amp, pan on twist.

Mft.twist(\freq, 0, 440, ControlSpec(20, 10000, \exp), {
	|val|
    Mft.post(\freq);
	if(f.isPlaying, { f.set(\freq, val)});
});

Mft.twist(\amp, 1, 0.2, ControlSpec(0.001, 1, \exp), {
	|val|
    Mft.post(\amp);
	if(f.isPlaying, { f.set(\amp, val)});
});

Mft.twist(\pan, 2, 0, ControlSpec(-1, 1, \lin), {
	|val|
    Mft.post(\pan);
	if(f.isPlaying, { f.set(\pan, val)});
});

// knob 15 is play/stop on press. strobes when playing.
Mft.toggle(\play, 15, {
	Mft.strobe(15);
    f = Synth(\simple, [
        \freq: Mft.get(\freq),
        \amp: Mft.get(\amp),
        \pan: Mft.get(\pan),
    ]);
    f.register();
}, {
	Mft.strobe(15, 0);
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

