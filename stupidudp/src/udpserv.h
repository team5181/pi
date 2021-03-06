#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <sys/socket.h>

#define PORT 1153
#define BUFSIZE 256

struct sockaddr_in myaddr;      /* our address */
struct sockaddr_in remaddr;     /* remote address */
socklen_t addrlen = sizeof(remaddr);            /* length of addresses */
int recvlen;                    /* # bytes received */
int fd;                         /* our socket */
unsigned char buf[BUFSIZE];     /* receive buffer */
bool controller= true; /*controller*/

void set_bin_addr() {
  memset((char *)&myaddr, 0, sizeof(myaddr));
  myaddr.sin_family = AF_INET;
  myaddr.sin_addr.s_addr = htonl(INADDR_ANY);
  myaddr.sin_port = htons(PORT);
}

void svrLoop() {
  while(controller) {
          printf("waiting on port %d\n", PORT);
          recvlen = recvfrom(fd, buf, BUFSIZE, 0, (struct sockaddr *)&remaddr, &addrlen);
          printf("received %d bytes\n", recvlen);
          if (recvlen > 0) {
                  buf[recvlen] = 0;
                  printf("received message: \"%s\"\n", buf);
          }
  }
}

int udp_serv() {
        /* create a UDP socket */

        if ((fd = socket(AF_INET, SOCK_DGRAM, 0)) < 0) {
                perror("cannot create socket\n");
                return 0;
        }

        /* bind the socket to any valid IP address and a specific port */
        set_bin_addr();

        if (bind(fd, (struct sockaddr *)&myaddr, sizeof(myaddr)) < 0) {
                perror("bind failed");
                return 0;
        }

        /* now loop, receiving data and printing what we received */
        svrLoop();
}
