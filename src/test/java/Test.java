import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Test {

    @org.junit.Test
    public void test() {
        int tweetLimit = 20;
        String inputString = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum";
        inputString += inputString;

        StringTokenizer stringTokenizer = new StringTokenizer(inputString, " ");
        List<String> wordList = new ArrayList<>();
        while(stringTokenizer.hasMoreTokens()) {
            wordList.add(stringTokenizer.nextToken() + " ");
        }

        List<Tweet> tweetList = buildTweets(tweetLimit, wordList);

        for (Tweet t: tweetList) {
            int counterDigits = adjustTweetBy(t);
            t.setDebug(t.getSentence().length() + "");
            System.out.println("Initial Tweet: " + t);
            List<Tweet> newTweetList = buildTweets(tweetLimit - counterDigits, wordList);
            if (newTweetList.size() == tweetList.size()) {
                tweetList = newTweetList;
                break;
            }
        }

        //Concat the counters with the tweet
        List<Tweet> finalList = new ArrayList<>();
        for (Tweet t: tweetList) {
            t.setSentence(t.getSentence() + t.getNum() + "/" + t.getDen());
            t.setDebug(t.getSentence().length() + ""); //debug
            finalList.add(t);
            System.out.println("Final Tweet: " + t);
            Assert.assertTrue(t.getSentence().length() <= tweetLimit);
        }



    }

    public List<Tweet> buildTweets(final int tweetLimit, final List<String> wordList) {
        String sentence = new String();
        List<Tweet> tweetList = new ArrayList<>();
        for (String w: wordList) {
            String newSentence = this.addWordToSentenceUpTo(tweetLimit, sentence, w);
            if (newSentence.equals(sentence)) {
                Tweet tweet = new Tweet();
                tweet.setSentence(sentence);
                tweetList.add(tweet);
                sentence = "";
            } else {
                sentence = newSentence;
            }
        }
        int i = 1;
        for (Tweet t: tweetList) {
            t.setDen(tweetList.size());
            t.setNum(i++);
        }
        return tweetList;
    }

    private int adjustTweetBy(final Tweet tweet) {
        int counterDigits = this.findNumOfDigits(tweet.getNum())
                + 1 // for /
                + this.findNumOfDigits(tweet.getDen());

        return counterDigits;
    }

    private int findNumOfDigits(int num) {
        if (num > 1000000) {
            return 7;
        } else if (num > 100000) {
            return 6;
        } else if (num > 10000) {
            return 5;
        } else if (num > 1000) {
            return 4;
        } else if (num > 100) {
            return 3;
        } else if (num > 10) {
            return 2;
        } else {
            return 1;
        }
    }

    private String addWordToSentenceUpTo(final int maxLength, final String sentence, final String word) {
        StringBuffer stringBuffer = new StringBuffer(sentence);
        if (sentence.length() + word.length() -1 > maxLength) {
            //do nothing
        } else {
            stringBuffer.append(word);
        }
        return stringBuffer.toString();
    }

    @Getter
    @Setter
    @ToString
    public static class Tweet {
        private String sentence;
        private int num;
        private int den;
        private String debug;
    }
}
