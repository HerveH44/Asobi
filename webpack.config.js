const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
  entry: {
    app: './src/main/resources/static/index.js'
  },
  devServer: {
    hot: true,
    port: 9001,
    contentBase: './built/static'
  },
  output: {
    path: path.join(__dirname, './built/static'),
    filename: '[name].bundle.js'
  },
  plugins: [
    new webpack.NamedModulesPlugin(),
    new webpack.HotModuleReplacementPlugin(),
    new HtmlWebpackPlugin({
      template: './src/main/resources/static/templates/index.html',
      filename: 'index.html',
      hash: true
    })
  ],
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
