#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <errno.h>
#include <stdio.h>
#include <netinet/in.h>
#include <string.h>
#include <signal.h>
#include <pthread.h>

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;

int attack_on = 0;

typedef struct thread_param {
	int client_fd;
} thread_param_t;

void* handle_client(void* arg);

void toggle_attack(int sig){
	
	pthread_mutex_lock(&mutex);
	attack_on = attack_on ^ 1;
	pthread_mutex_unlock(&mutex);
	
	if (attack_on){
		pthread_cond_broadcast(&cond);
	}

	printf("attack_on = %d\n",attack_on);
	
}

int main(int argc, char* argv[]) {
	struct sockaddr_in address;
	struct sockaddr_storage client_addr;
	socklen_t client_addrlen;
	int optval = 1;
	int sock_fd;
	int client_fd;
	pthread_t thread_dummy;
	int i;
	short port = 3000;
	signal(SIGINT, toggle_attack);
	sock_fd = socket(PF_INET, SOCK_STREAM, 0);


	if (sock_fd == -1) {
		perror("Socket:");
		return 1;
	}

	memset(&address, 0, sizeof(address));
	address.sin_family = AF_INET;
	port = htons(port);
	address.sin_port = port;

	if (setsockopt(sock_fd, SOL_SOCKET, SO_REUSEADDR, &optval, sizeof(optval)) == -1) {
		perror("setsockopt");
	}

	if (bind(sock_fd, (struct sockaddr*)&address, sizeof(address)) == -1) {
		perror("Bind:");
	}


	if (listen(sock_fd, 0) == -1) {
		perror("Listen:");
	}

	while (1) {
		client_fd = accept(sock_fd, (struct sockaddr*)&client_addr, &client_addrlen);
		if (client_fd != -1){
			pthread_create(&thread_dummy, NULL, handle_client, (void*)client_fd);
		}
	}
	return 0;
}


void* handle_client(void* arg) {
	int client_fd = (int)arg;
	int current_val;
	int sent_byte = 0;	
	printf("Someone connected!\n");

	while(1){
		pthread_mutex_lock(&mutex);
		pthread_cond_wait(&cond, &mutex);
		current_val = attack_on;
		pthread_mutex_unlock(&mutex);

		if(current_val == 1){
			send(client_fd, &current_val, 1, 0);
			close(client_fd);
		}
	}

	return NULL;

}
