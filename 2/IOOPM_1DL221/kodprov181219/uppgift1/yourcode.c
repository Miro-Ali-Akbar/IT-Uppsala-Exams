#include <string.h>
#include <stdbool.h>

/// \returns true if c is a character in the string delimiters
bool is_delimiter(int c, char *delimiters)
{
  return strchr(delimiters, c) != NULL;
}

/// TODO: Change original back to a single string using replacements
/// \param original a string chopped into multiple substrings by ioopm_strtok
/// \param an array of integers where every odd index holds a character and every even index holds the place in original where the preceding odd character should be placed. The end is marked by an odd index holding 0.
/// \returns number of replacements (same as number of pairs) in replacements
int ioopm_undo_strtok(char *original, int *replacements)
{
  /// Write your code here!
  int replacement_count = 0;
  for (int i = 0; replacements[i] != 0; i+=2) {
    char change_to = replacements[i];
    int where = replacements[i+1];

    *(original + where) = change_to;
    replacement_count ++;
  }

  return replacement_count; 
}

/// TODO: Implement in accordance with the specification 
/// \param src a null-terminated string or NULL if we want to continue tokenisation of the current string
/// \param delimiters the delimiters used to tokenise src
/// \param a pointer to replacements (see documentation in ioopm_undo_strtok) -- you can assume it has enough space
/// \returns the next token in the string 
char *ioopm_strtok(char *src, char *delimiters, int *replacements)
{
  /// Du får ändra på och kasta bort all nedanstående kod om du vill.
  /// OBS! Dessa variabler behåller sitt värde mellan funktionsanrop!
  static char *stored_src;       /// Används när src == NULL
  static char *stored_start;     /// Värdet på src första gånger
  static int replacement_index; /// Kan användas för att indexera replacements
  static bool first;
  static int count;
  if (src)
    {
      /// Spara strängen
      stored_src = src;
      stored_start = src;
      replacement_index = 0;
      first = true;
      count = 0;
    }
  else
    {
      /// Om src == NULL, använd den sparade strängen 
      src = stored_src;
      first = false;
    }

  /// Write your code here!
  while (!is_delimiter(*stored_src, delimiters) && *stored_src != '\0') {
    stored_src ++;
    count++;
  }
  if (*stored_src != '\0') {
    replacements[replacement_index++] = *stored_src;
    replacements[replacement_index++] = count;
    *stored_src = '\0';
    stored_src += 2;
    count += 2;
  }  
  if (first) {
    if (*stored_start == '\0') {
      return NULL;
    }
    return stored_start;
  }
  if (*src == '\0') {
    replacements[replacement_index] = 0;
    return NULL;
  }
  return src;
  
  
  /// Tips: du kan använda två loopar -- en för att skippa onödiga tecken i starten och en för att hitta slutet på tokenen 
  
  
}
