#define _GNU_SOURCE

#include <stdlib.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include "yourcode.h"

struct priority_queue
{
  link_t *first;
};

static link_t *link_create(link_t *next, void *element, int priority)
{
  link_t *link = malloc(sizeof(link_t));
  link->next = next;
  link->element = element;
  link->priority = priority;
  return link;
}

priority_queue_t *priority_queue_create()
{
  priority_queue_t *priority_queue = malloc(sizeof(priority_queue_t));
  priority_queue->first = link_create(NULL, NULL, -1); /// Dummy!
  return priority_queue;
}

void priority_queue_insert_with_priority(priority_queue_t *priority_queue, void *element, int priority)
{

  link_t *current = priority_queue->first;

  if (!current) 
    {
      priority_queue->first = link_create(NULL, element, priority);
      return;
    }

  
  while (current->next && current->next->priority <= priority)
    {
      current = current->next;
    }

  if (!current->next) 
    {
      current->next = link_create(NULL, element, priority);
    }
  else
    {
      current->next = link_create(current->next, element,priority);
    }
}

int priority_queue_size(priority_queue_t *priority_queue)
{
  /// Returnera antalet element i priority queue. Detta skall räknas ut
  /// genom iteration över samtliga länkar.

  int result = 0;
  link_t *current = priority_queue->first->next; // Skip dummy

  while (current)
    {
      result++;
      current = current->next;
    }
  
  return result; 
}

static void destroy_links(link_t *link)
{
  if (!link)
    return;
    
  destroy_links(link->next);

  free(link->element);
  free(link);
  
}

void priority_queue_destroy(priority_queue_t *priority_queue)
{
  /// Ta bort priority queue, alla länkar och alla länkars alla element
  destroy_links(priority_queue->first);
  free(priority_queue);  
}


void priority_queue_merge(priority_queue_t *source, priority_queue_t *dest)
{

  while (source->first->next)
    {
      link_t *current = source->first->next;
      source->first->next = current->next;
      current->next = NULL;

      link_t *first_dest = dest->first;

      while (first_dest->next && first_dest->next->priority <= current->priority)
        {
          first_dest = first_dest->next;
        }
      current->next = first_dest->next;
      first_dest->next = current;     
    }
}


//remove the element from the queue that has the highest priority, and return it
link_t *priority_queue_pull_highest_priority_element(priority_queue_t *priority_queue){


  link_t *to_return = priority_queue->first->next;
  if (to_return)
    priority_queue->first->next = to_return->next;
  
  return to_return;
}



/// Returns a string representation of the priority queue to be printed
/// No need to read or change this function!!
char *priority_queue_print(priority_queue_t *priority_queue, elem_to_string to_string)
{
  char *result = calloc(2048, sizeof(char));
  strcat(result, "[");
  
  for (link_t *cursor = priority_queue->first->next; cursor; cursor = cursor->next)
    {
      char *tmp = NULL;
      asprintf(&tmp, "(%d,%s), ", cursor->priority, to_string ? to_string(cursor->element) : (char *)cursor->element);
      strcat(result, tmp);
      free(tmp);
    }

  int size = strlen(result);
  strcpy(result + (size > 1 ? size - 2 : size), "]");
  
  return result;
}

/// Return the first link in the priority_queue for testing
/// No need to read or change this function!!
link_t *priority_queue_first(priority_queue_t *priority_queue)
{
  return priority_queue->first->next;
}


