const path = require('path');

module.exports = {
    entry: {
        app: './src/main/resources/static/index.js'
    },
    output: {
        path: path.join(__dirname, './src/main/resources/static/lib'),
        filename: '[name].bundle.js'
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: [
                    'babel-loader'
                ]
            }
        ]
    }
};