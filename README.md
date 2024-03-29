# SHAI

Command line tool that uses Google's Gemini Pro model with [Google AI Studio's](https://ai.google.dev/docs) rest API to generate a shell
command from a natural language description of a task.

https://github.com/galuque/shai/assets/7518032/da50640a-a020-4aef-bb83-276c80eaa993

## Warning

This tools is just a toy project, and always remember to be careful when running commands generated by an AI model. Never run commands that you don't understand. 

Only you are responsible for the commands you run on your system.

## Getting the CLI tool

### Prerequisites

You need a an API key to use the Gemini API, you can get one by following the instructions [here](https://ai.google.dev/tutorials/rest_quickstart#set_up_your_api_key).

Then you need to set the `GOOGLE_AI_STUDIO_API_KEY` environment variable to the value of your API key.

```bash
export GOOGLE_AI_STUDIO_API_KEY=<your API key>
```

or add the following line to your `~/.bashrc` or `~/.zshrc` file:

```bash
echo "export GOOGLE_AI_STUDIO_API_KEY=<your API key>" >> ~/.bashrc
source ~/.bashrc
```

### Downloading the tool from GitHub

```bash
curl -LO https://github.com/galuque/shai/releases/download/v0.1.29/shai-0.1.29-linux-static-amd64 > shai
chmod +x shai
./shai <query>

# or move the file to a directory in your PATH to make it available from anywhere
sudo mv shai /usr/local/bin
```

## Running the tool from source

Clone the repository and navigate to the project directory:

```bash
git clone https://github.com/galuque/shai.git
cd shai
```

Also, you need to have the following installed:
- [Clojure CLI](https://clojure.org/guides/getting_started)
- [Babashka](https://babashka.org/) (Optional, for running the tool with babashka)
- [GraalVM](https://www.graalvm.org/) (Optional, for building native executables with the `native-image` command)

### Clojure

```bash
clojure -M -m shai.cli <query>
```

### Babashka

Ensure that you have the `bb` ([babashka](https://babashka.org/)) command line tool installed. Then run the following command:  

```bash
bb -m shai.cli <query>
```

### Java JAR

First build the JAR file with the following command:
```bash
clojure -T:build uber
```

Then run the JAR file with the following command:
```bash
java -jar target/shai.cli-*.jar <query>
```

### Native Executable

First build the native executable with the following command:
```bash
clojure -T:build uber && bash native.sh
```

Then run the native executable with the following command:
```bash
./target/shai <query>
```
