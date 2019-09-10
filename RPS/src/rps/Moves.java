package rps;

public class Moves {
	public enum Move {
		ROCK {
			@Override
			public Move cycle() {
				return values()[1];
			}
		}, PAPER {
			@Override
			public Move cycle() {
				return values()[2];
			}
		}, SCISSORS {
			@Override
			public Move cycle() { // prevents cycling to ERROR
				return values()[0];
			}
		}, ERROR {
			@Override
			public Move cycle() {
				return values()[0];
			}
		};
		
		public abstract Move cycle();
	}
}
