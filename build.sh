#!/bin/bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
DEPS_DIR="$SCRIPT_DIR/deps"
CLASS_DIR="$SCRIPT_DIR/bin/class"

classpath_delim=":" # ':' on UNIX and ';' on Windows
classpath=".${classpath_delim}res${classpath_delim}${CLASS_DIR}"
declare -a JAR_URLS=(
    "https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar"
    "https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"
)

TEST_MODE=false

while [[ $# -gt 0 ]]; do
    case $1 in
        test)
            TEST_MODE=true
            shift
            ;;
    esac
done

cd GameProject

for url in "${JAR_URLS[@]}"
do
    v=$(echo "$url" | sed -r 's,^.*/(.*\.jar)$,\1,g')

    if [ ! -f "$SCRIPT_DIR/deps/$v" ]; then
        curl $url --output "$DEPS_DIR/$v"
    fi

done

case "$(uname -sr)" in 
    CYGWIN*|MINGW*|MINGW32*|MSYS*)
        classpath_delim=";"
    ;;
esac

javac -cp $classpath $( find "$SCRIPT_DIR" -type f ! -name "*Test.java" -name "*.java" ) -d $CLASS_DIR

if [ "${TEST_MODE}" = true ]; then
    classpath+="$classpath_delim$DEPS_DIR/*"
    javac -cp $classpath $( find "$SCRIPT_DIR" -type f -name "*Test.java" ) -d $CLASS_DIR

    for filename in $( find "$SCRIPT_DIR" -type f -name "*Test.java" ); do
        classname=$( echo "$filename" | sed -r 's,.*(GameProject.*)\.java$,\1,g' | sed -r 's,/+,.,g' )

        java -ea -cp $classpath org.junit.runner.JUnitCore $classname
        echo # newline
    done
else
    java -ea -cp $classpath main.Main
fi
