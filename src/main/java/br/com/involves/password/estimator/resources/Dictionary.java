package br.com.involves.password.estimator.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.involves.password.estimator.utils.ListStringComparator;

/**
 * Object used for dictionary matching.  This allows users to implement custom dictionaries for different languages
 * or specialized vocabulary.
 * <p>
 * Dictionaries need to use all lower case keys for the words they contain for the algorithm to work correctly.
 *
 * @author Adam Brusselback.
 */
public class Dictionary
{
    private final String dictionary_name;
    private final Map<String, Integer> dictonary;
    private final ArrayList<String> sorted_dictionary;
    private final Map<Integer, Integer> sorted_dictionary_length_lookup;
    private final boolean exclusion;


    /**
     * Object used for dictionary matching.
     *
     * @param dictionary_name unique name of dictionary.
     * @param dictonary       {@code Map} with the word and it's rank.  The key must be lowercase for the matching to work properly.
     * @param exclusion       {@code true} when desiring to disallow any password contained in this dictionary; {@code false} otherwise.
     */
    public Dictionary(final String dictionary_name, final Map<String, Integer> dictonary, final boolean exclusion)
    {
        this.dictionary_name = dictionary_name;
        this.dictonary = dictonary;
        this.exclusion = exclusion;

        // This is to optimize the distance calculation stuff
        ArrayList<String> unsorted_dictionary = new ArrayList<>(this.dictonary.keySet());
        this.sorted_dictionary = ListStringComparator.order(unsorted_dictionary);
        this.sorted_dictionary_length_lookup = new HashMap<>();
        for (int i = 0; i < sorted_dictionary.size(); i++) {
            String key = sorted_dictionary.get(i);
            if (sorted_dictionary_length_lookup.containsKey(key.length())) {
                continue;
            } else {
                sorted_dictionary_length_lookup.put(key.length(), i);
            }
        }

        for (int i = 0; i < sorted_dictionary_length_lookup.size(); i++)
        {
            if (!sorted_dictionary_length_lookup.containsKey(i))
            {
                int next_key = i;
                while (!sorted_dictionary_length_lookup.containsKey(next_key))
                {
                    next_key++;
                }
                sorted_dictionary_length_lookup.put(i, sorted_dictionary_length_lookup.get(next_key));
            }
        }
    }

    /**
     * The values within this dictionary.
     *
     * @return key = values in the dictionary; value = rank
     */
    public Map<String, Integer> getDictonary()
    {
        return dictonary;
    }

    /**
     * This contains the same values as in getDictionary, but is sorted for optimizing the speed
     * of the distance calculation
     *
     * @return A list of dictionary values sorted by length then alphabetical
     */
    public List<String> getSortedDictionary()
    {
        return sorted_dictionary;
    }

    /**
     * A map containing different lengths, and the first index they appear in the sorted dictionary.
     *
     * @return key = length; value = first index that length appears
     */
    public Map<Integer, Integer> getSortedDictionaryLengthLookup()
    {
        return sorted_dictionary_length_lookup;
    }

    /**
     * Returns if this dictionary is used for password exclusion or not. <br> <br>
     * If true, a password which matches to one of the values in the dictionary will always return 0 entropy for the portion which matches.
     *
     * @return true if excluded
     */
    public boolean isExclusion()
    {
        return exclusion;
    }

    /**
     * A description of the values contained in the dictionary.
     *
     * @return The dictionary name
     */
    public String getDictionaryName()
    {
        return this.dictionary_name;
    }
}
