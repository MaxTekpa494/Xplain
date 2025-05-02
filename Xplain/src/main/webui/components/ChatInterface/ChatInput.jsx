import {createEffect} from "solid-js";

export const ChatInput = (props) => {
    let textareaRef;

    createEffect(() => {
        if (textareaRef) {
            textareaRef.style.height = 'auto';
            textareaRef.style.height = `${textareaRef.scrollHeight}px`;
        }
    });

    return (
        <div class="field has-addons" style={{'flex-shrink': 0}}>
            <div class="control is-expanded">
        <textarea
            ref={textareaRef}
            className="textarea"
            placeholder="Tapez votre message (supporte le Markdown)"
            value={props.inputMessage()}
            onInput={(e) => {
                props.onInput(e.target.value);
                e.target.style.height = 'auto';
                e.target.style.height = `${e.target.scrollHeight}px`;
            }}
            disabled={props.isStreaming()}
            style={{
                'resize': 'none',
                'min-height': '24px',
                'max-height': '150px',
                'overflow-y': 'auto',
                'padding': '0.25rem 0.5rem',
                'line-height': '1.0'
            }}
        />
            </div>
            <div class="control" style={{'align-self': 'flex-end'}}>
                <button
                    class="button is-primary"
                    onClick={props.onSend}
                    disabled={props.isStreaming()}
                >
          <span class="icon">
            <i class="fas fa-paper-plane"></i>
          </span>
                    <span>{props.isStreaming() ? 'En cours...' : 'Envoyer'}</span>
                </button>
            </div>
        </div>
    );
};