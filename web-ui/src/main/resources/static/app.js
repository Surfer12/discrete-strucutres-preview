/**
 * Discrete Logic Mathematics Web UI
 * JavaScript for enhanced user interactions and React migration readiness
 */

// DOM Content Loaded Event
document.addEventListener('DOMContentLoaded', function() {
    console.log('Discrete Logic Mathematics Web UI - JavaScript loaded');
    
    // Initialize the application
    initializeApp();
    
    // Add fade-in animation to cards
    animateCards();
    
    // Setup form enhancements
    setupFormEnhancements();
    
    // Setup navigation highlighting
    setupNavigation();
    
    // Setup interactive features
    setupInteractiveFeatures();
});

/**
 * Initialize the application
 */
function initializeApp() {
    console.log('Initializing Discrete Logic Mathematics Web UI');
    
    // Check if we're ready for React components
    const reactRoot = document.getElementById('react-root');
    if (reactRoot) {
        console.log('React root element found - ready for React migration');
        // Future React component mounting will happen here
    }
    
    // Add loading states for forms
    addLoadingStates();
}

/**
 * Add fade-in animation to cards
 */
function animateCards() {
    const cards = document.querySelectorAll('.card');
    cards.forEach((card, index) => {
        card.style.animationDelay = `${index * 0.1}s`;
        card.classList.add('fade-in');
    });
}

/**
 * Setup form enhancements
 */
function setupFormEnhancements() {
    // Add real-time validation feedback
    const inputs = document.querySelectorAll('input[type="text"]');
    inputs.forEach(input => {
        input.addEventListener('input', function() {
            validateInput(this);
        });
        
        input.addEventListener('blur', function() {
            validateInput(this);
        });
    });
    
    // Add form submission handling
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            handleFormSubmission(this, e);
        });
    });
}

/**
 * Validate input fields
 */
function validateInput(input) {
    const value = input.value.trim();
    
    if (input.hasAttribute('required') && !value) {
        input.classList.add('is-invalid');
        input.classList.remove('is-valid');
        return false;
    }
    
    // Specific validation for expression inputs
    if (input.name === 'expression' && value) {
        if (validateLogicalExpression(value)) {
            input.classList.add('is-valid');
            input.classList.remove('is-invalid');
        } else {
            input.classList.add('is-invalid');
            input.classList.remove('is-valid');
        }
    } else if (value) {
        input.classList.add('is-valid');
        input.classList.remove('is-invalid');
    }
    
    return true;
}

/**
 * Basic logical expression validation
 */
function validateLogicalExpression(expression) {
    // Basic validation for logical operators
    const validOperators = ['AND', 'OR', 'NOT', 'XOR', 'NAND', 'NOR', 'IMPLIES', 'BICONDITIONAL'];
    const hasValidOperator = validOperators.some(op => expression.toUpperCase().includes(op));
    
    // Check for balanced parentheses
    const openParens = (expression.match(/\(/g) || []).length;
    const closeParens = (expression.match(/\)/g) || []).length;
    
    return hasValidOperator && openParens === closeParens;
}

/**
 * Handle form submission
 */
function handleFormSubmission(form, event) {
    console.log('Form submission:', form.action);
    
    // Add loading state
    const submitBtn = form.querySelector('button[type="submit"]');
    if (submitBtn) {
        const originalText = submitBtn.innerHTML;
        submitBtn.innerHTML = '<span class="loading"></span> Processing...';
        submitBtn.disabled = true;
        
        // Re-enable after a delay (in case of error)
        setTimeout(() => {
            submitBtn.innerHTML = originalText;
            submitBtn.disabled = false;
        }, 5000);
    }
}

/**
 * Setup navigation highlighting
 */
function setupNavigation() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-link');
    
    navLinks.forEach(link => {
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
}

/**
 * Setup interactive features
 */
function setupInteractiveFeatures() {
    // Add hover effects for cards
    const cards = document.querySelectorAll('.card');
    cards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });
    
    // Add click handlers for common operations
    setupQuickOperations();
    
    // Add keyboard shortcuts
    setupKeyboardShortcuts();
}

/**
 * Setup quick operations
 */
function setupQuickOperations() {
    // Quick insert buttons for logical operators
    const expressionInputs = document.querySelectorAll('input[name="expression"]');
    expressionInputs.forEach(input => {
        addQuickOperationButtons(input);
    });
}

/**
 * Add quick operation buttons
 */
function addQuickOperationButtons(input) {
    const operators = ['AND', 'OR', 'NOT', 'XOR', 'IMPLIES'];
    const container = input.parentElement;
    
    const buttonGroup = document.createElement('div');
    buttonGroup.className = 'btn-group btn-group-sm mt-2';
    buttonGroup.setAttribute('role', 'group');
    
    operators.forEach(op => {
        const button = document.createElement('button');
        button.type = 'button';
        button.className = 'btn btn-outline-secondary';
        button.textContent = op;
        button.addEventListener('click', function() {
            insertOperator(input, op);
        });
        buttonGroup.appendChild(button);
    });
    
    container.appendChild(buttonGroup);
}

/**
 * Insert operator into expression input
 */
function insertOperator(input, operator) {
    const cursorPos = input.selectionStart;
    const textBefore = input.value.substring(0, cursorPos);
    const textAfter = input.value.substring(cursorPos);
    
    // Add spaces around operator for readability
    const operatorText = ` ${operator} `;
    
    input.value = textBefore + operatorText + textAfter;
    input.focus();
    input.setSelectionRange(cursorPos + operatorText.length, cursorPos + operatorText.length);
    
    // Trigger validation
    validateInput(input);
}

/**
 * Setup keyboard shortcuts
 */
function setupKeyboardShortcuts() {
    document.addEventListener('keydown', function(e) {
        // Ctrl+Enter to submit active form
        if (e.ctrlKey && e.key === 'Enter') {
            const activeForm = document.activeElement.closest('form');
            if (activeForm) {
                activeForm.submit();
            }
        }
        
        // Escape to clear active input
        if (e.key === 'Escape') {
            const activeInput = document.activeElement;
            if (activeInput && activeInput.tagName === 'INPUT') {
                activeInput.value = '';
                validateInput(activeInput);
            }
        }
    });
}

/**
 * Add loading states
 */
function addLoadingStates() {
    // Add loading utility functions for future React components
    window.DiscreteLogicUI = {
        showLoading: function(element) {
            element.classList.add('loading');
        },
        hideLoading: function(element) {
            element.classList.remove('loading');
        },
        validateExpression: validateLogicalExpression
    };
}

/**
 * Utility functions for React migration
 */
window.DiscreteLogicUtils = {
    // Expression validation
    validateLogicalExpression: validateLogicalExpression,
    
    // Form utilities
    validateInput: validateInput,
    
    // Animation utilities
    animateElement: function(element, animationClass = 'fade-in') {
        element.classList.add(animationClass);
    },
    
    // API utilities (for future backend integration)
    apiCall: function(endpoint, data) {
        return fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .catch(error => {
            console.error('API Error:', error);
            throw error;
        });
    }
};

// Export for module systems (when migrating to React)
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        DiscreteLogicUI: window.DiscreteLogicUI,
        DiscreteLogicUtils: window.DiscreteLogicUtils
    };
}