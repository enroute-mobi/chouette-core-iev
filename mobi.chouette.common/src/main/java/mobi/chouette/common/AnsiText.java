package mobi.chouette.common;

public class AnsiText {

	public enum Attribute {
		NORMAL(0), BRIGHT(1), DIM(2), UNDERLINE(4), BLINK(5), REVERSE(7), HIDDEN(8);

		private String value;

		private Attribute(int value) {
			this.value = String.valueOf(value);
		}

		@Override
		public String toString() {
			return "" + value;
		}
	}

	public enum Color {
		BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE
	}

	private static final String PREFIX = "\u001b["; // NOI18N
	private static final String SUFFIX = "m";
	private static final String SEPARATOR = ";";
	private static final String END = PREFIX + SUFFIX;
	private String text;
	private Attribute attribute;
	private Color foreground;
	private Color background;

	private AnsiText(String text) {
		this.text = text;
		attribute = Attribute.NORMAL;
	}

	public static AnsiText create(String text) {
		return new AnsiText(text);
	}

	public AnsiText attribute(Attribute att) {
		this.attribute = att;
		return this;
	}

	public AnsiText fg(Color foreground) {
		this.foreground = foreground;
		return this;
	}

	public AnsiText bg(Color background) {
		this.background = background;
		return this;
	}

	public String toString() {
		StringBuilder buff = new StringBuilder();

		if (attribute != null) {
			buff.append(attribute);
		}

		if (foreground != null) {
			if (buff.length() > 0) {
				buff.append(SEPARATOR);
			}
			buff.append(30 + foreground.ordinal());
		}

		if (background != null) {
			if (buff.length() > 0) {
				buff.append(SEPARATOR);
			}
			buff.append(40 + background.ordinal());
		}

		buff.insert(0, PREFIX);
		buff.append(SUFFIX);
		buff.append(text);
		buff.append(END);
		return buff.toString();
	}

}
