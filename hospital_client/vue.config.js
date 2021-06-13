const fs = require('fs')

module.exports = {
    devServer: {
    port:3030,
    host: 'localhost',
    https: true,
    key: fs.readFileSync('.\\root.key'),
    cert: fs.readFileSync('.\\root.crt'),
    hotOnly: false,
    }
}