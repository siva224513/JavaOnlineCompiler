let mode = document.getElementById("theme-toggle");
let reset = document.getElementById("reset");
let run = document.getElementById("run-code");
let clear = document.getElementById("clear-output");
let codeArea = document.querySelector(".codingArea");
let output = document.querySelector(".outputArea");

let basicCode =
  `// Java Compiler\n` +
  `// Write your Java code here\n\n` +
  `class Main{\n` +
  `    public static void main(String[] args) {\n` +
  `        System.out.println("Hello World");\n` +
  `    }\n` +
  `}`;

window.onload = function () {
  codeArea.innerText = basicCode;
};

reset.addEventListener("click", function () {
  codeArea.innerText = basicCode;
});

mode.addEventListener("click", () => {
  if (mode.innerText === "Dark Mode") {
    document.body.style.backgroundColor = "#2d2d2d";
    codeArea.style.backgroundColor = "#1e1e1e";
    codeArea.style.color = "#ffffff";
    output.style.backgroundColor = "#1e1e1e";
    output.style.color = "#a6e22e";
    mode.innerText = "Light Mode";
  } else {
    document.body.style.backgroundColor = "#f0f0f0";
    codeArea.style.backgroundColor = "#d8d5d5";
    codeArea.style.color = "#000000";
    output.style.backgroundColor = "#d8d5d5";
    output.style.color = "#000000";
    mode.innerText = "Dark Mode";
  }
});

clear.addEventListener("click", () => {
  output.innerText = "";
});

const makeRequest = async () => {
  output.innerText = "Running...";
  const codeContent = codeArea.innerText;

  try {
    const response = await fetch(
      "/JavaOnlineCompiler/JavaCompiler",
      {
        method: "POST",

        body: JSON.stringify({ code: codeContent }),
      }
    );

    if (response.ok) {
      const result = await response.json();

      if (result.compilationError) {
        output.innerText = `Compilation Error:\n${result.compilationError}`;
      } else if (result.error) {
        output.innerText = `Execution Error:\n${result.error}`;
      } else if (result.output) {
        output.innerText = `Output:\n${result.output}`;
      }
    } else {
      output.innerText = "Error: Failed to run the code.";
    }
  } catch (error) {
    output.innerText = `Error: ${error.message}`;
  }
};

run.addEventListener("click", makeRequest);
