compile:
	gcc -Wall -g driver.c yourcode.c

test: compile
	./a.out

memtest: compile
	valgrind --leak-check=full ./a.out

clean:
	rm -f a.out
