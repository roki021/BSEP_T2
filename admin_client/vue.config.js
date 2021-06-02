const fs = require('fs')

module.exports = {
	    devServer: {
		        port:3000,
		        host: 'localhost',
		        https: true,
		        key: fs.readFileSync('.\\root.key'),
		        cert: fs.readFileSync('.\\root.crt'),
		        https: true,
		        hotOnly: false,
	    }
}
