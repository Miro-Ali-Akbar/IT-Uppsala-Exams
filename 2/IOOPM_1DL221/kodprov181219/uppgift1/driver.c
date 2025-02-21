#include <stdlib.h>
#include <stdio.h>
#include <string.h>

extern int ioopm_undo_strtok(char *original, int *replacements);
extern char *ioopm_strtok(char *src, char *delimiters, int *replacements);

static int failures = 0;

void test1()
{
  int *replacements = calloc(1024, sizeof(int));
  char *orig = "Fee, foo, fi, fum!";
  char *str = strdup(orig);
  printf("Original:    '%s'\n", str);
  char *delimiters = ", ";
  char *substring = ioopm_strtok(str, delimiters, replacements);

  printf("Substrings:  ");
  while (substring)
    {
      printf("'%s' ", substring);
      substring = ioopm_strtok(NULL, delimiters, replacements);
    }
  printf("\n");

  printf("Before undo: '%s'\n", str);
  int no_replacements = ioopm_undo_strtok(str, replacements);
  printf("After undo:  '%s' (%d replacements)\n", str, no_replacements);

  if (strcmp(orig, str) == 0)
    {
      puts("PASSED .. String rebuilt correctly!");
    }
  else
    {
      ++failures;
      puts("FAILED .. String NOT rebuilt correctly!");
    }

  if (no_replacements == 3)
    {
      puts("PASSED .. Correct number of replacements!");
    }
  else
    {
      ++failures;
      puts("FAILED .. Wrong number of replacements");
    }
    
  free(str);
  free(replacements);
}

void test2()
{
  int *replacements = calloc(1024, sizeof(int));
  char *orig = "Han var: som laxhandare; oöverträffad!";
  char *str = strdup(orig);
  printf("Original:    '%s'\n", str);
  char *delimiters = ":;";
  char *substring = ioopm_strtok(str, delimiters, replacements);

  printf("Substrings:  ");
  while (substring)
    {
      printf("'%s' ", substring);
      substring = ioopm_strtok(NULL, delimiters, replacements);
    }
  printf("\n");

  printf("Before undo: '%s'\n", str);
  int no_replacements = ioopm_undo_strtok(str, replacements);
  printf("After undo:  '%s' (%d replacements)\n", str, no_replacements);

  if (strcmp(orig, str) == 0)
    {
      puts("PASSED .. String rebuilt correctly!");
    }
  else
    {
      ++failures;
      puts("FAILED .. String NOT rebuilt correctly!");
    }

  if (no_replacements == 2)
    {
      puts("PASSED .. Correct number of replacements!");
    }
  else
    {
      ++failures;
      puts("FAILED .. Wrong number of replacements");
    }
    
  free(str);
  free(replacements);
}

void test3()
{
  char *orig = "abcde";
  char backwards[] = "a\0c\0e";
  int replacements[] = { 'b', 1, 'd', 3, 0 };
  int no_replacements = ioopm_undo_strtok(backwards, replacements);

  if (strcmp(orig, backwards) == 0)
    {
      puts("PASSED .. String rebuilt correctly!");
    }
  else
    {
      ++failures;
      printf("FAILED .. String NOT rebuilt correctly! ('%s')\n", backwards);
    }

  if (no_replacements == 2)
    {
      puts("PASSED .. Correct number of replacements!");
    }
  else
    {
      ++failures;
      puts("FAILED .. Wrong number of replacements");
    }
}

void test4()
{
  int *replacements = calloc(1024, sizeof(int));
  char orig[] = "";
  char *substring = ioopm_strtok(orig, " ", replacements);

  if (substring == NULL)
    {
      puts("PASSED .. no substring found (correct)");
    }
  else
    {
      ++failures;
      puts("FAILED .. check test4 for more information");
    }

  free(replacements);
}

void test5()
{
  int *replacements = calloc(1024, sizeof(int));
  char orig[] = " ";
  char *substring = ioopm_strtok(orig, " ", replacements);

  if (substring == NULL)
    {
      puts("PASSED .. no substring found (correct)");
    }
  else
    {
      ++failures;
      puts("FAILED .. check test5 for more information");
    }

  free(replacements);
}

void test6()
{
  int *replacements = calloc(1024, sizeof(int));
  char orig[] = "   ";
  char *substring = ioopm_strtok(orig, " ", replacements);

  if (substring == NULL)
    {
      puts("PASSED .. no substring found (correct)");
    }
  else
    {
      ++failures;
      puts("FAILED .. check test5 for more information");
    }

  free(replacements);
}

int main(void)
{
  puts("--[ Test 1 ]----------------");
  test1();
  puts("--[ Test 2 ]----------------");
  test2();
  puts("--[ Test 3 ]----------------");
  test3();
  puts("--[ Test 4 ]----------------");
  test4();
  puts("--[ Test 5 ]----------------");
  test5();
  puts("--[ Test 6 ]----------------");
  test6();

  if (failures == 0)
    {
      puts("\n\tAll tests pass!");
    }
  else
    {
      printf("\n\tThere are %d FAILING tests -- check the output above!\n", failures);
    }
  
  return 0;
}
