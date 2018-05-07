const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const Visualizer = require('webpack-visualizer-plugin');

module.exports = {
    entry: {
        app: './src/main/js/index.js'
    },
    output: {
        path: path.join(__dirname, './built/static'),
        filename: 'bundles/[name].bundle.js',
        publicPath: "/"
    },
    devServer: {
        hot: true,
        port: 9001,
        contentBase: './built/static',
        proxy: {
            "/ws": "http://localhost:8080"
        }
    },
    plugins: [
        new webpack.NamedModulesPlugin(),
        new webpack.HotModuleReplacementPlugin(),
        new HtmlWebpackPlugin({
            template: './src/main/resources/templates/index.html',
            filename: 'index.html',
            hash: true
        }),
        new Visualizer()
    ],
    optimization: {
        splitChunks : {
            chunks: "all",
        }
    },
    module: {
        rules: [{
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: [
                    'babel-loader'
                ]
            },
            {
                test: /\.(jpe?g|png|gif|svg)$/,
                loader: 'url-loader'
            }
        ]
    }
};
