package br.com.involves.password.estimator.matching;

import org.junit.Assert;
import org.junit.Test;

import br.com.involves.password.estimator.matching.DictionaryMatcher;
import br.com.involves.password.estimator.matching.PasswordMatcher;
import br.com.involves.password.estimator.matching.match.DictionaryMatch;
import br.com.involves.password.estimator.matching.match.Match;
import br.com.involves.password.estimator.resources.Configuration;
import br.com.involves.password.estimator.resources.ConfigurationBuilder;
import br.com.involves.password.estimator.resources.Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Brusselback
 */
public class DictionaryMatcherTest
{
    final Configuration configuration;

    public DictionaryMatcherTest()
    {
        final List<Dictionary> dictionaries = new ArrayList<>();
        for (Dictionary dictionary : ConfigurationBuilder.getDefaultDictionaries())
        {
            if (!dictionary.getDictionaryName().equals("eff_large"))
            {
                dictionaries.add(dictionary);
            }
        }

        this.configuration = new ConfigurationBuilder().setDictionaries(dictionaries).createConfiguration();
    }


    /**
     * Test of match method, of class DictionaryMatcher, without leet value.
     */
    @Test
    public void testDictionaryMatchWithoutLeet()
    {
        System.out.println("Test of dictionaryMatch method (without leet value), of class DictionaryMatcher");

        PasswordMatcher matcher = new DictionaryMatcher();

        List<Match> computed = matcher.match(configuration, "password");

        List<Match> expected = new ArrayList<>();
        List<Character[]> empty = new ArrayList<>();

        expected.add(new DictionaryMatch("pas", configuration, 0, 2, "pas", 8458, empty, false, false, "english", 0));
        expected.add(new DictionaryMatch("pass", configuration, 0, 3, "pass", 75, empty, false, false, "passwords", 0));
        expected.add(new DictionaryMatch("pass", configuration, 0, 3, "pass", 1408, empty, false, false, "english", 0));
        expected.add(new DictionaryMatch("passw", configuration, 0, 4, "passw", 64655, empty, false, false, "passwords", 0));
        expected.add(new DictionaryMatch("passwo", configuration, 0, 5, "passwo", 49088, empty, false, false, "passwords", 0));
        expected.add(new DictionaryMatch("passwor", configuration, 0, 6, "passwor", 1441, empty, false, false, "passwords", 0));
        expected.add(new DictionaryMatch("password", configuration, 0, 7, "password", 2, empty, false, false, "passwords", 0));
        expected.add(new DictionaryMatch("password", configuration, 0, 7, "passmore", 3860, empty, false, true, "surnames", 2));
        expected.add(new DictionaryMatch("password", configuration, 0, 7, "password", 528, empty, false, false, "english", 0));
        expected.add(new DictionaryMatch("ass", configuration, 1, 3, "ass", 10015, empty, false, false, "passwords", 0));
        expected.add(new DictionaryMatch("ass", configuration, 1, 3, "ass", 1227, empty, false, false, "english", 0));
        expected.add(new DictionaryMatch("assword", configuration, 1, 7, "assword", 4678, empty, false, false, "passwords", 0));
        expected.add(new DictionaryMatch("swor", configuration, 3, 6, "rows", 7395, empty, false, true, "english", 0));
        expected.add(new DictionaryMatch("sword", configuration, 3, 7, "sword", 3647, empty, false, false, "passwords", 0));
        expected.add(new DictionaryMatch("sword", configuration, 3, 7, "sword", 6494, empty, false, false, "english", 0));
        expected.add(new DictionaryMatch("wor", configuration, 4, 6, "row", 2550, empty, false, true, "english", 0));
        expected.add(new DictionaryMatch("word", configuration, 4, 7, "word", 6455, empty, false, false, "passwords", 0));
        expected.add(new DictionaryMatch("word", configuration, 4, 7, "word", 745, empty, false, false, "english", 0));

        int computedHash = calcHash(computed);

        int expectedHash = calcHash(expected);

        Assert.assertEquals(expectedHash, computedHash);

    }


    /**
     * Test of match method, of class DictionaryMatcher, using a leet value.
     */
    @Test
    public void testDictionaryMatchWithLeet()
    {
        System.out.println("Test of dictionaryMatch method (with leet value), of class DictionaryMatcher");

        PasswordMatcher matcher = new DictionaryMatcher();

        // l33t
        List<Match> computed = matcher.match(configuration, "l33t");

        List<Match> expected = new ArrayList<>();

        List<Character[]> subs = new ArrayList<>();

        subs.add(new Character[]{'3', 'e'});
        subs.add(new Character[]{'3', 'e'});
        expected.add(new DictionaryMatch("l33", configuration, 0, 2, "lee", 65166, subs, false, false, "passwords", 0));

        expected.add(new DictionaryMatch("l33", configuration, 0, 2, "lee", 23, subs, false, false, "surnames", 0));

        expected.add(new DictionaryMatch("l33", configuration, 0, 2, "lee", 1746, subs, false, false, "english", 0));

        expected.add(new DictionaryMatch("l33t", configuration, 0, 3, "leet", 60382, subs, false, false, "passwords", 0));

        expected.add(new DictionaryMatch("l33t", configuration, 0, 3, "leet", 15746, subs, false, false, "surnames", 0));

        expected.add(new DictionaryMatch("33t", configuration, 1, 3, "tee", 5298, subs, false, true, "english", 0));

        int computedHash = calcHash(computed);

        int expectedHash = calcHash(expected);

        Assert.assertEquals(expectedHash, computedHash);


        // ch1(k3n
        computed = matcher.match(configuration, "ch1(k3n");

        expected = new ArrayList<>();

        subs = new ArrayList<>();

        subs.add(new Character[]{'1', 'i'});
        subs.add(new Character[]{'(', 'c'});
        subs.add(new Character[]{'3', 'e'});
        expected.add(new DictionaryMatch("ch1", configuration, 0, 2, "chi", 816, subs.subList(0, 1), false, false, "male_names", 0));

        expected.add(new DictionaryMatch("ch1", configuration, 0, 2, "chi", 5485, subs.subList(0, 1), false, false, "english", 0));

        expected.add(new DictionaryMatch("ch1(", configuration, 0, 3, "chic", 28085, subs.subList(0, 2), false, false, "passwords", 0));

        expected.add(new DictionaryMatch("ch1(", configuration, 0, 3, "chic", 17150, subs.subList(0, 2), false, false, "english", 0));

        expected.add(new DictionaryMatch("ch1(k", configuration, 0, 4, "chick", 6711, subs.subList(0, 2), false, false, "passwords", 0));

        expected.add(new DictionaryMatch("ch1(k", configuration, 0, 4, "chick", 7136, subs.subList(0, 2), false, false, "english", 0));

        expected.add(new DictionaryMatch("ch1(k3", configuration, 0, 5, "chicke", 23712, subs.subList(0, 3), false, false, "passwords", 0));

        expected.add(new DictionaryMatch("ch1(k3n", configuration, 0, 6, "chicken", 135, subs.subList(0, 3), false, false, "passwords", 0));

        expected.add(new DictionaryMatch("ch1(k3n", configuration, 0, 6, "chicken", 3436, subs.subList(0, 3), false, false, "english", 0));

        expected.add(new DictionaryMatch("h1(", configuration, 1, 3, "hic", 13053, subs.subList(0, 2), false, false, "english", 0));

        expected.add(new DictionaryMatch("h1(k3n", configuration, 1, 6, "hicken", 30392, subs.subList(0, 3), false, false, "surnames", 0));

        expected.add(new DictionaryMatch("k3n", configuration, 4, 6, "ken", 243, subs.subList(2, 3), false, false, "male_names", 0));

        expected.add(new DictionaryMatch("k3n", configuration, 4, 6, "ken", 4065, subs.subList(2, 3), false, false, "english", 0));

        computedHash = calcHash(computed);

        expectedHash = calcHash(expected);

        Assert.assertEquals(expectedHash, computedHash);

    }


    /**
     * Test of match method, of class DictionaryMatcher, using LD.
     */
    @Test
    public void testDictionaryMatchLD()
    {
        System.out.println("Test of dictionaryMatch method (with LD calculation), of class DictionaryMatcher");

        PasswordMatcher matcher = new DictionaryMatcher();

        List<Match> computed = matcher.match(configuration, "pxassworxd");

        List<Match> expected = new ArrayList<>();

        ArrayList<Character[]> subs = new ArrayList<>();

        expected.add(new DictionaryMatch("pxassworxd", configuration, 0, 9, "password", 2, subs, false, false, "passwords", 2));
        expected.add(new DictionaryMatch("pxassworxd", configuration, 0, 9, "password", 528, subs, false, false, "english", 2));
        expected.add(new DictionaryMatch("ass", configuration, 2, 4, "ass", 10015, subs, false, false, "passwords", 0));
        expected.add(new DictionaryMatch("ass", configuration, 2, 4, "ass", 1227, subs, false, false, "english", 0));
        expected.add(new DictionaryMatch("swor", configuration, 4, 7, "rows", 7395, subs, false, true, "english", 0));
        expected.add(new DictionaryMatch("wor", configuration, 5, 7, "row", 2550, subs, false, true, "english", 0));

        int computedHash = calcHash(computed);

        int expectedHash = calcHash(expected);

        Assert.assertEquals(expectedHash, computedHash);

    }

    private int calcHash(List<Match> matches)
    {
        int calculatedHash = 0;
        for (Match match_i : matches)
        {
            DictionaryMatch match = DictionaryMatch.class.cast(match_i);

            calculatedHash += match.getToken().hashCode();
            calculatedHash += match.getDictionaryName().hashCode();
            calculatedHash += match.getDictionaryValue().hashCode();
            calculatedHash += match.getRank();
            calculatedHash += match.getStartIndex();
            calculatedHash += match.getEndIndex();
            for (Character[] chars : match.getLeetSubstitution())
            {
                for (Character leet : chars)
                {
                    calculatedHash += leet.hashCode();
                }
            }
        }
        return calculatedHash;
    }

}
