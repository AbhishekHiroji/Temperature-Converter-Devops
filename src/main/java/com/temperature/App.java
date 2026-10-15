
package com.temperature;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class App {

    public static double celsiusToFahrenheit(double celsius) {
        return (celsius * 9.0 / 5.0) + 32.0;
    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32.0) * 5.0 / 9.0;
    }

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(
                System.getenv().getOrDefault("PORT", "8080"));

        HttpServer server = HttpServer.create(
                new InetSocketAddress("0.0.0.0", port), 0);

        server.createContext("/", exchange -> {
            String html = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1">
                  <title>Temperature Converter</title>
                  <style>
                    body { font-family: Arial; background:#eef2ff; text-align:center;
                           padding:50px 15px; }
                    .card { background:white; max-width:420px; margin:auto;
                            padding:28px; border-radius:14px; }
                    input,select,button { padding:12px; margin:8px;
                                          width:85%; box-sizing:border-box; }
                    button { background:#3949ab; color:white; border:0;
                             border-radius:6px; cursor:pointer; }
                    #result { font-weight:bold; margin-top:18px; }
                  </style>
                </head>
                <body>
                  <div class="card">
                    <h1>Temperature Converter</h1>
                    <p>Convert Celsius and Fahrenheit</p>
                    <input id="temperature" type="number" step="any"
                           placeholder="Enter temperature">
                    <select id="conversion">
                      <option value="CtoF">Celsius to Fahrenheit</option>
                      <option value="FtoC">Fahrenheit to Celsius</option>
                    </select>
                    <button id="convert" onclick="convertTemperature()">
                      Convert
                    </button>
                    <p id="result" aria-live="polite">Result will appear here</p>
                  </div>
                  <script>
                    function convertTemperature() {
                      const input = document.getElementById('temperature').value;
                      const result = document.getElementById('result');
                      if (input.trim() === '') {
                        result.textContent = 'Please enter a temperature';
                        return;
                      }
                      const value = Number(input);
                      const mode = document.getElementById('conversion').value;
                      const answer = mode === 'CtoF'
                        ? value * 9 / 5 + 32
                        : (value - 32) * 5 / 9;
                      const unit = mode === 'CtoF' ? '°F' : '°C';
                      result.textContent = 'Result: ' + Number(answer.toFixed(2)) + ' ' + unit;
                    }
                  </script>
                </body>
                </html>
                """;

            byte[] response = html.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set(
                    "Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.length);
            try (var output = exchange.getResponseBody()) {
                output.write(response);
            }
        });

        server.start();
        System.out.println("Temperature Converter running on port " + port);
    }
}
