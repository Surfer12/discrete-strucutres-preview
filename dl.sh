#!/bin/bash

# Enhanced Discrete Logic CLI Wrapper
# This script provides shortcuts and helpful features for the discrete logic tool

# Get the directory where this script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Path to the JAR file
JAR_PATH="$SCRIPT_DIR/target/discrete-logic-java-1.0.0.jar"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Check if the JAR file exists
check_jar() {
    if [ ! -f "$JAR_PATH" ]; then
        echo -e "${RED}Error: JAR file not found at $JAR_PATH${NC}"
        echo -e "${YELLOW}Please run 'mvn clean package' first to build the project.${NC}"
        exit 1
    fi
}

# Function to run the JAR with arguments
run_jar() {
    check_jar
    java -jar "$JAR_PATH" "$@"
}

# Show help with examples
show_help() {
    echo -e "${GREEN}Discrete Logic CLI - Enhanced Wrapper${NC}"
    echo
    echo "Usage: $0 [command] [options]"
    echo
    echo -e "${BLUE}Commands:${NC}"
    echo "  tt, truth-table     Generate truth tables"
    echo "  ba, boolean         Boolean algebra operations"
    echo "  lg, logic-gates     Logic gate operations"
    echo "  sets                Set operations"
    echo "  convert             Number system conversions"
    echo "  comb, combinatorics Combinatorics calculations"
    echo "  km, karnaugh        Karnaugh map operations"
    echo "  tutorial            Interactive tutorial"
    echo "  quiz                Interactive quiz"
    echo "  help                Show this help message"
    echo
    echo -e "${BLUE}Quick Examples:${NC}"
    echo
    echo -e "${YELLOW}Truth Tables:${NC}"
    echo "  $0 tt 'A & B'                    # AND operation"
    echo "  $0 tt 'A | B'                    # OR operation"
    echo "  $0 tt '!A'                       # NOT operation"
    echo "  $0 tt 'A -> B'                   # Implication"
    echo "  $0 tt 'A <-> B'                  # Biconditional"
    echo "  $0 tt '(A | B) -> C'             # Complex expression"
    echo
    echo -e "${YELLOW}Boolean Algebra:${NC}"
    echo "  $0 ba 'A & (B | C)' --simplify   # Simplify expression"
    echo "  $0 ba '!(!A | !B)' --dnf         # Convert to DNF"
    echo "  $0 ba 'A & B | C' --cnf          # Convert to CNF"
    echo
    echo -e "${YELLOW}Number Conversion:${NC}"
    echo "  $0 convert 42                    # Decimal to all systems"
    echo "  $0 convert 1010 --from binary --to decimal"
    echo "  $0 convert FF --from hex --to binary"
    echo
    echo -e "${YELLOW}Logic Gates:${NC}"
    echo "  $0 lg --list                     # List all gate types"
    echo "  $0 lg --gate AND                 # Show AND gate truth table"
    echo "  $0 lg --demo half-adder          # Demo half-adder circuit"
    echo
    echo -e "${YELLOW}Set Operations:${NC}"
    echo "  $0 sets                          # Interactive set operations demo"
    echo
    echo -e "${YELLOW}Combinatorics:${NC}"
    echo "  $0 comb -n 5 -r 3 --combinations # C(5,3)"
    echo "  $0 comb -n 10 --factorial        # 10!"
    echo "  $0 comb -n 5 --pascal            # Pascal's triangle"
    echo
    echo -e "${GREEN}Tips:${NC}"
    echo "  • Use single quotes (') around expressions to avoid shell issues"
    echo "  • For help on any command: $0 [command] --help"
    echo "  • Run without arguments to see the main help: $0"
}

# Main script logic
case "$1" in
    # Truth table shortcuts
    tt|truth-table)
        shift
        run_jar truth-table "$@"
        ;;

    # Boolean algebra shortcuts
    ba|boolean|bool)
        shift
        run_jar boolean-algebra "$@"
        ;;

    # Logic gates shortcuts
    lg|logic-gates|gates)
        shift
        run_jar logic-gates "$@"
        ;;

    # Set operations
    sets|set)
        shift
        run_jar sets "$@"
        ;;

    # Number conversion
    convert|conv)
        shift
        run_jar convert "$@"
        ;;

    # Combinatorics shortcuts
    comb|combinatorics)
        shift
        run_jar combinatorics "$@"
        ;;

    # Karnaugh map shortcuts
    km|karnaugh|kmap)
        shift
        run_jar karnaugh-map "$@"
        ;;

    # Tutorial
    tutorial|tut)
        shift
        run_jar tutorial "$@"
        ;;

    # Quiz
    quiz)
        shift
        run_jar quiz "$@"
        ;;

    # Help
    help|-h|--help)
        show_help
        ;;

    # Empty argument - show main program help
    "")
        run_jar
        ;;

    # Default - pass through to the JAR
    *)
        run_jar "$@"
        ;;
esac
