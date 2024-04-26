
package spamDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SpamDetector {

	private static List<String>	spamList	= List.of("sex[,\\s]+", "viagra[,\\s]+", "cialis[,\\s]+", "one\\s+million[,\\s]+", "you\\s+won[,\\s]+", "nigeria[,\\s]+", "sexo[,\\s]+", "un\\s+millon[,\\s]+", "has\\s+ganado[,\\s]+", "sex$", "viagra$",
		"cialis$", "one\\s+million$", "you\\s+won$", "nigeria$", "sexo$", "un\\s+millón$", "has\\s+ganado$");
	private static int			threshold	= 10;


	public static boolean checkTextValue(String text) {
		List<String> spamList = new ArrayList<>(SpamDetector.spamList);
		text = text.toLowerCase();
		int spamPoints = 0;
		for (String spam : spamList) {
			long pointsToAdd = SpamDetector.getValidCharsOfText(spam);
			Pattern pattern = Pattern.compile(spam);
			Matcher matcher = pattern.matcher(text);
			while (matcher.find())
				spamPoints += pointsToAdd;
		}
		return SpamDetector.threshold < (double) spamPoints / SpamDetector.getValidCharsOfText(text) * 100;
	}
	private static int getValidCharsOfText(String text) {
		if (SpamDetector.spamList.contains(text)) {
			text = text.replace("[,\\s]+", "");
			text = text.replace("\\s+", "");
			if (text.endsWith("$"))
				text = text.substring(0, text.length() - 1);
		}
		int count = 0;
		for (Character c : text.toCharArray())
			if (!c.equals(' '))
				count++;
		return count;
	}
	public static void setSpamList(final List<String> spamList) {
		if (spamList == null)
			return;
		SpamDetector.spamList = new ArrayList<>();
		for (int i = 0; i < spamList.size(); i++)
			SpamDetector.addSpam(spamList.get(i));
	}

	public static List<String> getSpamList() {
		return new ArrayList<>(SpamDetector.spamList.stream().map(spam -> {
			String replacedSpam = spam.toLowerCase().replace("\\s+", " ");
			replacedSpam = spam.toLowerCase().replace("[,\\s]+", " ");
			if (replacedSpam.endsWith("$"))
				replacedSpam = replacedSpam.substring(0, replacedSpam.length() - 1);
			return replacedSpam;
		}).collect(Collectors.toSet()));
	}

	public static int getThreshold() {
		return SpamDetector.threshold;
	}
	public static void addSpam(final String text) {
		if (text == null)
			return;
		String replacedSpam = text.toLowerCase().replace(" ", "\\s+");
		SpamDetector.spamList.add(replacedSpam + "[,\\s]+");
		SpamDetector.spamList.add(replacedSpam + "$");
	}
	public static void deleteSpam(final String text) {
		if (text == null)
			return;
		String replacedSpam = text.toLowerCase().replace(" ", "\\s+");
		SpamDetector.spamList.remove(replacedSpam + "[,\\s]+");
		SpamDetector.spamList.remove(replacedSpam + "$");
	}
	public static void setThreshold(final int threshold) {
		if (threshold < 0 || threshold > 100)
			return;
		SpamDetector.threshold = threshold;
	}
}
