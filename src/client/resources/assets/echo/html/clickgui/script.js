// Prevent default context menu
document.addEventListener('contextmenu', (e) => {
    e.preventDefault();
});

// ============================================
// CUSTOM SMOOTH SCROLL FOR MCEF COMPATIBILITY
// ============================================

class SmoothScroll {
    constructor(element, options = {}) {
        this.element = element;
        this.targetScrollTop = element.scrollTop;
        this.currentScrollTop = element.scrollTop;
        this.ease = options.ease || 0.1;
        this.isScrolling = false;
        this.rafId = null;
        this.wheelTimeout = null;

        this.init();
    }

    init() {
        // Handle wheel events
        this.element.addEventListener('wheel', (e) => {
            e.preventDefault();

            const delta = e.deltaY;
            this.targetScrollTop += delta;

            // Clamp target
            const maxScroll = this.element.scrollHeight - this.element.clientHeight;
            this.targetScrollTop = Math.max(0, Math.min(this.targetScrollTop, maxScroll));

            if (!this.isScrolling) {
                this.startScrolling();
            }

            // Reset timeout
            clearTimeout(this.wheelTimeout);
            this.wheelTimeout = setTimeout(() => {
                this.stopScrolling();
            }, 150);
        }, { passive: false });

        // Handle scrollbar drag
        let isDragging = false;
        this.element.addEventListener('mousedown', (e) => {
            if (e.offsetX > this.element.clientWidth - 20) {
                isDragging = true;
            }
        });

        document.addEventListener('mouseup', () => {
            if (isDragging) {
                isDragging = false;
                this.currentScrollTop = this.element.scrollTop;
                this.targetScrollTop = this.element.scrollTop;
            }
        });

        this.element.addEventListener('scroll', () => {
            if (isDragging) {
                this.currentScrollTop = this.element.scrollTop;
                this.targetScrollTop = this.element.scrollTop;
            }
        });
    }

    startScrolling() {
        this.isScrolling = true;
        this.animate();
    }

    stopScrolling() {
        if (Math.abs(this.targetScrollTop - this.currentScrollTop) < 0.5) {
            this.isScrolling = false;
            this.currentScrollTop = this.targetScrollTop;
            this.element.scrollTop = this.targetScrollTop;
            if (this.rafId) {
                cancelAnimationFrame(this.rafId);
                this.rafId = null;
            }
        }
    }

    animate() {
        if (!this.isScrolling) return;

        // Smooth interpolation
        this.currentScrollTop += (this.targetScrollTop - this.currentScrollTop) * this.ease;

        // Apply scroll
        this.element.scrollTop = this.currentScrollTop;

        // Continue animation
        this.rafId = requestAnimationFrame(() => this.animate());
    }

    scrollTo(target, duration = 800) {
        this.targetScrollTop = target;

        const start = this.currentScrollTop;
        const distance = target - start;
        const startTime = performance.now();

        const easeOutCubic = (t) => {
            return 1 - Math.pow(1 - t, 3);
        };

        const animateScrollTo = (currentTime) => {
            const elapsed = currentTime - startTime;
            const progress = Math.min(elapsed / duration, 1);
            const eased = easeOutCubic(progress);

            this.currentScrollTop = start + (distance * eased);
            this.element.scrollTop = this.currentScrollTop;

            if (progress < 1) {
                requestAnimationFrame(animateScrollTo);
            } else {
                this.currentScrollTop = target;
                this.targetScrollTop = target;
                this.element.scrollTop = target;
            }
        };

        requestAnimationFrame(animateScrollTo);
    }
}

// Initialize smooth scroll for all scrollable elements
let smoothScrollInstances = [];

function initSmoothScroll() {
    // Clear existing instances
    smoothScrollInstances = [];

    // Modules section
    const modulesSection = document.querySelector('.modules-section');
    if (modulesSection) {
        smoothScrollInstances.push(new SmoothScroll(modulesSection, { ease: 0.12 }));
    }

    // Settings sidebar
    const settingsSidebar = document.getElementById('settingsSidebar');
    if (settingsSidebar) {
        smoothScrollInstances.push(new SmoothScroll(settingsSidebar, { ease: 0.12 }));
    }

    // Chat messages
    const chatMessages = document.getElementById('chatMessages');
    if (chatMessages) {
        smoothScrollInstances.push(new SmoothScroll(chatMessages, { ease: 0.15 }));
    }

    // Config panels
    document.querySelectorAll('.config-content').forEach(content => {
        if (content.scrollHeight > content.clientHeight) {
            smoothScrollInstances.push(new SmoothScroll(content, { ease: 0.12 }));
        }
    });
}

// Initialize on load
window.addEventListener('load', () => {
    initSmoothScroll();
});

// Re-initialize when panels change
const observer = new MutationObserver(() => {
    setTimeout(initSmoothScroll, 100);
});

observer.observe(document.body, {
    childList: true,
    subtree: true
});

// ============================================
// END OF CUSTOM SMOOTH SCROLL
// ============================================

// Global state
let isLoggedIn = false;
let selectedComponent = 'clickgui';
let chatHistory = [];

// Tab switching
document.querySelectorAll('.tab').forEach(tab => {
    tab.addEventListener('click', function() {
        const tabName = this.dataset.tab;

        // Update tabs
        document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
        this.classList.add('active');

        // Update content
        document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
        document.querySelector(`[data-content="${tabName}"]`).classList.add('active');
    });
});

// Module toggle and settings
let selectedModule = null;

document.querySelectorAll('.module').forEach(module => {
    // Left click - toggle module
    module.addEventListener('click', function(e) {
        if (e.button === 0) {
            this.classList.toggle('active');
            // Add ripple effect
            createRipple(e, this);
        }
    });

    // Right click - open settings
    module.addEventListener('contextmenu', function(e) {
        e.preventDefault();
        e.stopPropagation();

        selectedModule = this.dataset.module;
        const moduleName = this.querySelector('.module-name').textContent;
        showSettings(moduleName, selectedModule);
    });
});

// Ripple effect
function createRipple(event, element) {
    const ripple = document.createElement('span');
    const rect = element.getBoundingClientRect();
    const size = Math.max(rect.width, rect.height);
    const x = event.clientX - rect.left - size / 2;
    const y = event.clientY - rect.top - size / 2;

    ripple.style.cssText = `
        position: absolute;
        width: ${size}px;
        height: ${size}px;
        left: ${x}px;
        top: ${y}px;
        background: radial-gradient(circle, rgba(99, 102, 241, 0.4) 0%, transparent 70%);
        border-radius: 50%;
        pointer-events: none;
        animation: ripple-animation 0.6s ease-out;
    `;

    const style = document.createElement('style');
    style.textContent = `
        @keyframes ripple-animation {
            to {
                transform: scale(2);
                opacity: 0;
            }
        }
    `;
    document.head.appendChild(style);

    element.appendChild(ripple);
    setTimeout(() => ripple.remove(), 600);
}

function showSettings(displayName, moduleName) {
    const sidebar = document.getElementById('settingsSidebar');
    sidebar.classList.add('active');

    const settingsHTML = `
        <div class="settings-title">
            <div class="back-btn" onclick="document.getElementById('settingsSidebar').classList.remove('active')">←</div>
            ${displayName} Settings
        </div>

        <div class="settings-grid">
            <div class="setting">
                <div class="setting-label">Enabled</div>
                <div class="toggle active"></div>
            </div>

            <div class="setting">
                <div class="setting-label">AutoBlock</div>
                <div class="toggle active"></div>
            </div>

            <div class="setting">
                <div class="setting-label">Range</div>
                <div class="slider-container">
                    <div class="slider">
                        <div class="slider-fill"></div>
                        <div class="slider-thumb"></div>
                    </div>
                    <div class="slider-value">4.2 blocks</div>
                </div>
            </div>

            <div class="setting">
                <div class="setting-label">Rotation Speed</div>
                <div class="slider-container">
                    <div class="slider">
                        <div class="slider-fill" style="width: 75%"></div>
                        <div class="slider-thumb" style="left: 75%"></div>
                    </div>
                    <div class="slider-value">180°/s</div>
                </div>
            </div>

            <div class="setting">
                <div class="setting-label">Speed</div>
                <div class="slider-container">
                    <div class="slider">
                        <div class="slider-fill" style="width: 60%"></div>
                        <div class="slider-thumb" style="left: 60%"></div>
                    </div>
                    <div class="slider-value">12.0 CPS</div>
                </div>
            </div>

            <div class="setting">
                <div class="setting-label">Max Angle</div>
                <div class="slider-container">
                    <div class="slider">
                        <div class="slider-fill" style="width: 45%"></div>
                        <div class="slider-thumb" style="left: 45%"></div>
                    </div>
                    <div class="slider-value">45°</div>
                </div>
            </div>

            <div class="setting">
                <div class="setting-label">Mode</div>
                <div class="mode-selector">
                    <div class="mode-option active">Single</div>
                    <div class="mode-option">Multi</div>
                    <div class="mode-option">Switch</div>
                </div>
            </div>

            <div class="setting">
                <div class="setting-label">Priority</div>
                <div class="mode-selector">
                    <div class="mode-option active">Distance</div>
                    <div class="mode-option">Health</div>
                    <div class="mode-option">Armor</div>
                </div>
            </div>

            <div class="setting">
                <div class="setting-label">Target ESP</div>
                <div class="toggle"></div>
            </div>

            <div class="setting">
                <div class="setting-label">Players</div>
                <div class="toggle active"></div>
            </div>

            <div class="setting">
                <div class="setting-label">Animals</div>
                <div class="toggle"></div>
            </div>

            <div class="setting">
                <div class="setting-label">Mobs</div>
                <div class="toggle active"></div>
            </div>

            <div class="setting">
                <div class="setting-label">Invisibles</div>
                <div class="toggle"></div>
            </div>

            <div class="setting">
                <div class="setting-label">Teams</div>
                <div class="toggle"></div>
            </div>

            <div class="setting">
                <div class="setting-label">Ray Trace</div>
                <div class="toggle active"></div>
            </div>

            <div class="setting">
                <div class="setting-label">Keep Sprint</div>
                <div class="toggle active"></div>
            </div>
        </div>
    `;

    document.getElementById('settings-content').innerHTML = settingsHTML;

    // Re-init toggle listeners
    document.querySelectorAll('.toggle').forEach(toggle => {
        toggle.addEventListener('click', function() {
            this.classList.toggle('active');
        });
    });

    // Re-init mode selector
    document.querySelectorAll('.mode-option').forEach(option => {
        option.addEventListener('click', function() {
            this.parentElement.querySelectorAll('.mode-option').forEach(o => o.classList.remove('active'));
            this.classList.add('active');
        });
    });

    // Re-init sliders
    initSliders();
}

function initSliders() {
    document.querySelectorAll('.slider-container').forEach(container => {
        const slider = container.querySelector('.slider');
        const thumb = container.querySelector('.slider-thumb');
        const fill = container.querySelector('.slider-fill');
        let isDragging = false;

        thumb.addEventListener('mousedown', () => {
            isDragging = true;
        });

        document.addEventListener('mouseup', () => {
            isDragging = false;
        });

        document.addEventListener('mousemove', (e) => {
            if (isDragging) {
                const rect = slider.getBoundingClientRect();
                let x = e.clientX - rect.left;
                x = Math.max(0, Math.min(x, rect.width));
                const percentage = (x / rect.width) * 100;
                thumb.style.left = percentage + '%';
                fill.style.width = percentage + '%';
            }
        });

        slider.addEventListener('click', (e) => {
            const rect = slider.getBoundingClientRect();
            let x = e.clientX - rect.left;
            x = Math.max(0, Math.min(x, rect.width));
            const percentage = (x / rect.width) * 100;
            thumb.style.left = percentage + '%';
            fill.style.width = percentage + '%';
        });
    });
}

// Module Search
const searchInput = document.getElementById('moduleSearch');
const searchCount = document.getElementById('searchCount');

searchInput.addEventListener('input', function() {
    const searchTerm = this.value.toLowerCase().trim();
    const allModules = document.querySelectorAll('.module');
    let visibleCount = 0;

    allModules.forEach(module => {
        const searchData = module.dataset.search || '';
        const moduleName = module.querySelector('.module-name').textContent.toLowerCase();

        if (searchTerm === '' || searchData.includes(searchTerm) || moduleName.includes(searchTerm)) {
            module.classList.remove('hidden');
            visibleCount++;
        } else {
            module.classList.add('hidden');
        }
    });

    if (searchTerm) {
        searchCount.textContent = `${visibleCount} found`;
    } else {
        searchCount.textContent = '';
    }
});

// Login System
const loginBtn = document.getElementById('loginBtn');
const logoutBtn = document.getElementById('logoutBtn');
const loginForm = document.getElementById('loginForm');
const loginSuccess = document.getElementById('loginSuccess');
const userStatus = document.getElementById('userStatus');
const usernameInput = document.getElementById('username');
const passwordInput = document.getElementById('password');

loginBtn.addEventListener('click', function() {
    const username = usernameInput.value.trim();
    const password = passwordInput.value.trim();

    if (username === '123' && password === 'admin') {
        // Successful login
        isLoggedIn = true;
        loginForm.style.display = 'none';
        loginSuccess.style.display = 'block';

        // Update user status
        userStatus.classList.add('logged-in');
        userStatus.querySelector('.status-text').textContent = `Logged in as ${username}`;

        // Show online configs
        document.getElementById('configLoginRequired').style.display = 'none';
        document.getElementById('onlineConfigs').style.display = 'block';

        // Add success animation
        loginSuccess.style.animation = 'fadeInScale 0.5s cubic-bezier(0.16, 1, 0.3, 1)';
    } else {
        // Failed login - shake animation
        loginForm.style.animation = 'shake 0.5s';
        setTimeout(() => {
            loginForm.style.animation = '';
        }, 500);

        // Add shake keyframes
        if (!document.querySelector('#shake-style')) {
            const style = document.createElement('style');
            style.id = 'shake-style';
            style.textContent = `
                @keyframes shake {
                    0%, 100% { transform: translateX(0); }
                    10%, 30%, 50%, 70%, 90% { transform: translateX(-10px); }
                    20%, 40%, 60%, 80% { transform: translateX(10px); }
                }
            `;
            document.head.appendChild(style);
        }

        // Show error
        usernameInput.style.borderColor = 'var(--danger)';
        passwordInput.style.borderColor = 'var(--danger)';

        setTimeout(() => {
            usernameInput.style.borderColor = '';
            passwordInput.style.borderColor = '';
        }, 2000);
    }
});

logoutBtn.addEventListener('click', function() {
    isLoggedIn = false;
    loginForm.style.display = 'block';
    loginSuccess.style.display = 'none';

    // Clear inputs
    usernameInput.value = '';
    passwordInput.value = '';

    // Update user status
    userStatus.classList.remove('logged-in');
    userStatus.querySelector('.status-text').textContent = 'Not logged in';

    // Hide online configs
    document.getElementById('configLoginRequired').style.display = 'block';
    document.getElementById('onlineConfigs').style.display = 'none';
});

// Config Tabs
document.querySelectorAll('.config-tab').forEach(tab => {
    tab.addEventListener('click', function() {
        const tabName = this.dataset.configTab;

        // Update tabs
        document.querySelectorAll('.config-tab').forEach(t => t.classList.remove('active'));
        this.classList.add('active');

        // Update content
        document.querySelectorAll('.config-content').forEach(c => c.classList.remove('active'));
        document.querySelector(`[data-config-content="${tabName}"]`).classList.add('active');
    });
});

// Component Selection
document.querySelectorAll('.component-item').forEach(item => {
    item.addEventListener('click', function() {
        // Deselect all
        document.querySelectorAll('.component-item').forEach(i => {
            i.classList.remove('active');
            i.querySelector('.component-status').textContent = 'Available';
        });

        // Select this one
        this.classList.add('active');
        this.querySelector('.component-status').textContent = 'Selected';
        selectedComponent = this.dataset.component;

        // Update AI message
        addAIMessage(`Component changed to <strong>${this.querySelector('.component-name').textContent}</strong>. How would you like to customize it?`);
    });
});

// Chat System
const chatMessages = document.getElementById('chatMessages');
const chatInput = document.getElementById('chatInput');
const sendBtn = document.getElementById('sendBtn');

function addMessage(text, isUser = false) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `chat-message ${isUser ? 'user-message' : 'ai-message'}`;

    const now = new Date();
    const timeStr = now.toLocaleTimeString('pl-PL', { hour: '2-digit', minute: '2-digit' });

    messageDiv.innerHTML = `
        <div class="message-avatar">
            ${isUser ?
                '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>' :
                '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2v20M2 12h20M17 7l-10 10M7 7l10 10"/></svg>'
            }
        </div>
        <div class="message-content">
            <div class="message-header">
                <span class="message-sender">${isUser ? 'You' : 'Echo AI'}</span>
                <span class="message-time">${timeStr}</span>
            </div>
            <div class="message-text">${text}</div>
        </div>
    `;

    chatMessages.appendChild(messageDiv);

    // Use custom smooth scroll to bottom
    const smoothScroll = smoothScrollInstances.find(s => s.element === chatMessages);
    if (smoothScroll) {
        setTimeout(() => {
            smoothScroll.scrollTo(chatMessages.scrollHeight, 600);
        }, 50);
    } else {
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }
}

function addAIMessage(text) {
    addMessage(text, false);
}

function addUserMessage(text) {
    addMessage(text, true);
}

function simulateAIResponse(userMessage) {
    // Simulate AI thinking
    const thinkingDiv = document.createElement('div');
    thinkingDiv.className = 'chat-message ai-message';
    thinkingDiv.id = 'thinking-message';
    thinkingDiv.innerHTML = `
        <div class="message-avatar">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M12 2v20M2 12h20M17 7l-10 10M7 7l10 10"/>
            </svg>
        </div>
        <div class="message-content">
            <div class="message-header">
                <span class="message-sender">Echo AI</span>
                <span class="message-time">Just now</span>
            </div>
            <div class="message-text">
                <div style="display: flex; gap: 4px; align-items: center;">
                    <div style="width: 8px; height: 8px; background: var(--primary); border-radius: 50%; animation: pulse 1.5s ease-in-out infinite;"></div>
                    <div style="width: 8px; height: 8px; background: var(--primary); border-radius: 50%; animation: pulse 1.5s ease-in-out 0.2s infinite;"></div>
                    <div style="width: 8px; height: 8px; background: var(--primary); border-radius: 50%; animation: pulse 1.5s ease-in-out 0.4s infinite;"></div>
                </div>
            </div>
        </div>
    `;
    chatMessages.appendChild(thinkingDiv);

    // Use custom smooth scroll to bottom
    const smoothScroll = smoothScrollInstances.find(s => s.element === chatMessages);
    if (smoothScroll) {
        setTimeout(() => {
            smoothScroll.scrollTo(chatMessages.scrollHeight, 600);
        }, 50);
    } else {
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    // Simulate delay and response
    setTimeout(() => {
        thinkingDiv.remove();

        const lowerMessage = userMessage.toLowerCase();
        let response = '';

        if (lowerMessage.includes('purple') || lowerMessage.includes('violet') || lowerMessage.includes('fioletow')) {
            response = `Great choice! I'll update the <strong>${getComponentName()}</strong> with a purple color scheme. The primary color will be adjusted to various shades of purple with smooth gradient transitions. This will give your client a distinctive and elegant look.`;
        } else if (lowerMessage.includes('dark') || lowerMessage.includes('ciemn')) {
            response = `Perfect! I'll apply a darker theme to the <strong>${getComponentName()}</strong>. The background opacity will be increased, and colors will be adjusted for better contrast in dark environments. Your client will have a sleek, modern appearance.`;
        } else if (lowerMessage.includes('animation') || lowerMessage.includes('animacj')) {
            response = `Excellent! I'll enhance the <strong>${getComponentName()}</strong> with smooth animations. Expect subtle hover effects, transition animations, and micro-interactions that will make the interface feel more responsive and polished.`;
        } else if (lowerMessage.includes('minimal') || lowerMessage.includes('simple') || lowerMessage.includes('prosty')) {
            response = `Understood! I'll simplify the <strong>${getComponentName()}</strong> design. I'll reduce visual clutter, use cleaner lines, and focus on essential elements only. The result will be a minimalist, distraction-free interface.`;
        } else if (lowerMessage.includes('neon') || lowerMessage.includes('glow') || lowerMessage.includes('świec')) {
            response = `Awesome! I'll add neon glow effects to the <strong>${getComponentName()}</strong>. Expect vibrant colors with glowing borders, text shadows, and pulsing animations. Your client will have a futuristic, cyberpunk aesthetic.`;
        } else if (lowerMessage.includes('reset') || lowerMessage.includes('default') || lowerMessage.includes('domyśln')) {
            response = `The <strong>${getComponentName()}</strong> has been reset to default settings. All customizations have been removed. Feel free to describe a new design direction!`;
        } else {
            response = `I understand you want to customize the <strong>${getComponentName()}</strong>. Could you be more specific about what you'd like to change? For example, you can mention:
            <br><br>
            • Colors (e.g., "make it purple", "darker theme")
            <br>
            • Animations (e.g., "add smooth animations")
            <br>
            • Style (e.g., "minimal design", "neon glow effect")
            <br>
            • Layout changes
            <br><br>
            I'm here to help bring your vision to life!`;
        }

        addAIMessage(response);
    }, 1500);
}

function getComponentName() {
    const componentMap = {
        'clickgui': 'ClickGUI',
        'targethud': 'TargetHUD',
        'arraylist': 'ArrayList'
    };
    return componentMap[selectedComponent] || 'Component';
}

sendBtn.addEventListener('click', sendMessage);
chatInput.addEventListener('keypress', function(e) {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        sendMessage();
    }
});

function sendMessage() {
    const message = chatInput.value.trim();
    if (!message) return;

    addUserMessage(message);
    chatInput.value = '';

    // Auto-resize
    chatInput.style.height = 'auto';

    // Simulate AI response
    simulateAIResponse(message);
}

// Reset Design Button
document.getElementById('resetDesignBtn').addEventListener('click', function() {
    if (confirm('Are you sure you want to reset the design to default? This action cannot be undone.')) {
        addAIMessage(`The <strong>${getComponentName()}</strong> design has been reset to default settings. All customizations have been removed.`);

        // Add visual feedback
        this.style.transform = 'scale(0.95)';
        setTimeout(() => {
            this.style.transform = '';
        }, 200);
    }
});

// Close button
document.querySelector('.close-btn').addEventListener('click', () => {
    document.querySelector('.main-window').style.animation = 'windowAppear 0.3s cubic-bezier(0.16, 1, 0.3, 1) reverse';
    setTimeout(() => {
        console.log('Closing GUI...');
        // In real implementation, this would close the window
    }, 300);
});

// Keyboard shortcuts
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
        const sidebar = document.getElementById('settingsSidebar');
        if (sidebar.classList.contains('active')) {
            sidebar.classList.remove('active');
        } else {
            document.querySelector('.close-btn').click();
        }
    }

    // Module keybinds
    document.querySelectorAll('.module').forEach(module => {
        const key = module.querySelector('.module-key').textContent;
        if (e.key.toUpperCase() === key.toUpperCase() && key !== '-') {
            module.classList.toggle('active');
            createRipple({
                clientX: module.getBoundingClientRect().left + module.offsetWidth / 2,
                clientY: module.getBoundingClientRect().top + module.offsetHeight / 2
            }, module);
        }
    });
});

// Auto-resize chat input
chatInput.addEventListener('input', function() {
    this.style.height = 'auto';
    this.style.height = (this.scrollHeight) + 'px';
});

// Initialize
console.log('Echo Client v3.2.1 Premium - Initialized');
console.log('Login credentials: username: 123, password: admin');