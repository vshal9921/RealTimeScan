const aedes = require('aedes')();
const server = require('net').createServer(aedes.handle);
const port = 1883;

server.listen(port, function () {
  console.log('server listening on port', port);
});

setInterval(() => {
  aedes.clients.forEach(client => {
    const message = JSON.stringify({ timestamp: Date.now() });
    client.publish({ topic: 'server/msg', payload: message, qos: 2 }, () => {
      console.log('Message sent to', client.id);
    });
  });
}, 2000);
