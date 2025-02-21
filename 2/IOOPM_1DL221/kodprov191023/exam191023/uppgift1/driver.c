#include <string.h>
#include "yourcode.h"

/// strdup equivalent since it does not always work on some machines
char *str(char *in)
{
  int len = strlen(in);
  char *out = calloc(len + 1, sizeof(char));
  for (int i = 0; i < len; ++i)
    {
      out[i] = in[i];
    }
  return out;
}

void create_random_queues(priority_queue_t **queue1, priority_queue_t **queue2)
{
  *queue1 = priority_queue_create();
  *queue2 = priority_queue_create();
                     
  char *strings[] = {"a", "b", "c", "d", "e", "f", "g", "h",
                     "i", "j", "k", "l", "m", "n", "o", "p",
                     "q", "r", "s", "t", "u", "v", "w", "x",
                     "y", "z"};
  int no_strings = 26;
  
  for (size_t input_size = 3 + (rand() % 3); input_size; --input_size)
    {
      int item = rand() % no_strings;
      int priority = rand() % 5;
      priority_queue_insert_with_priority(*queue1, str(strings[item]), priority);
      strings[item] = strings[--no_strings]; 
    }
  
  for (size_t input_size = 3 + (rand() % 3); input_size; --input_size)
    {
      int item = rand() % no_strings;
      int priority = rand() % 5;
      priority_queue_insert_with_priority(*queue2, str(strings[item]), priority);
      strings[item] = strings[--no_strings]; 
    }
}

void print(priority_queue_t *priority_queue)
{
    char *str = priority_queue_print(priority_queue, NULL);
    printf("'%s'\n", str);
    free(str);
}

bool link_check(link_t *links[], int size, link_t *first)
{
 for (int i = 0; i < size; ++i)
    {
      link_t *cursor = first;
      while (cursor)
        {
          if (cursor == links[i]) break;
          cursor = cursor->next;
        }
      if (cursor == NULL)
        {
          printf("Missing link with element %s\n", (char *)links[i]->element);
          return false; 
        }
    }
  return true;
}

void queue_test()
{
  priority_queue_t *a = NULL; 
  priority_queue_t *b = NULL;
  create_random_queues(&a, &b);
  int size_a = priority_queue_size(a);
  int size_b = priority_queue_size(b);
  int size = size_a + size_b;

  puts("First random queue 'a'");
  print(a);
  puts("Second random queue 'b'");
  print(b);

  /// Recording pointer values
  link_t *links[size];
  int i = 0;
  for (link_t *cursor = priority_queue_first(a); cursor; cursor = cursor->next)
    {
      links[i++] = cursor; 
    }
  for (link_t *cursor = priority_queue_first(b); cursor; cursor = cursor->next)
    {
      links[i++] = cursor; 
    }
  
  puts("Merging queues");
  priority_queue_merge(a, b);

  puts("Expecting size of 'a' to be zero");
  printf("%d == %d ... %s\n", priority_queue_size(a), 0, (priority_queue_size(a) == 0 ? "PASSED" : "FAILED"));
  puts("Expecting size of 'b' to be sum of 'a' and 'b' before merge");
  printf("%d == %d ... %s\n", priority_queue_size(b), size, (priority_queue_size(b) == size ? "PASSED" : "FAILED"));

  puts("Expecting the output of 'a' to be []");
  print(a);
  puts("Expecting the output of 'b' to be all elements of 'a' and 'b' before merge in ascending order of priorities (e.g. (1,c) < (2,a), etc.)");
  print(b);

  puts("Checking that the links, not just elements were moved");
  printf("... %s\n", link_check(links, size, priority_queue_first(b)) ? "PASSED" : "FAILED");
  
  priority_queue_destroy(a);
  priority_queue_destroy(b);
}  

int main(void)
{
  for (int i = 0; i < 3; ++i)
    {
      puts("=========================== TEST START =================================");
      queue_test();
      puts("=========================== TEST STOP ==================================\n");
    }
  
  return 0;
}
