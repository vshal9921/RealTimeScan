const aedes = require('aedes')();
const server = require('net').createServer(aedes.handle);
const port = 1883;

server.listen(port, function () {
  console.log('server listening on port', port);
});

setInterval(() => {
  // Prepare the message
  const message = {
    topic: 'server/msg',
    payload: JSON.stringify({ timestamp: Date.now() }),
    qos: 2, // Quality of Service level
    retain: false // Retain the message
  };

  // Publish the message to all clients
  for (const client of Object.values(aedes.clients)) {
    aedes.publish(message, client, (err) => {
      if (err) {
        console.error('Failed to publish message', err);
      } else {
        console.log('Message sent to', client.id);
      }
    });
  }
}, 2000);
