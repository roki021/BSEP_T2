const fs = require('fs')

module.exports = {
    devServer: {
    port:3030,
    host: '127.0.0.1',
    https: false,
    key: fs.readFileSync('.\\root.key'),
    cert: fs.readFileSync('.\\root.crt'),
    hotOnly: false,
    }
}