import { marked } from 'marked';
import Prism from 'prismjs';
import 'prismjs/themes/prism-tomorrow.css';
import {createSignal, Show} from "solid-js";

export const MessageComponent = (props) => {
    const [isEditing, setIsEditing] = createSignal(false);
    const [editContent, setEditContent] = createSignal(props.message.content);

    const handleEdit = async () => {
        if(props.message.sender === 'Marx') {
            await props.onEdit?.(editContent(), true);
        }else{
            props.onEdit?.(editContent(), false);
        }
        setIsEditing(false);
    }

    const renderMarkdown = (content) => {
        // Configure marked pour utiliser Prism pour la coloration syntaxique
        marked.setOptions({
            highlight: (code, lang) => {
                if (lang && Prism.languages[lang]) {
                    return Prism.highlight(code, Prism.languages[lang], lang);
                }
                return code;
            }
        });

        // Détecte si le contenu commence et finit par des backticks pour du code
        if (content.startsWith('```') && content.endsWith('```')) {
            const language = content.split('\n')[0].replace('```', '').trim();
            const code = content.slice(content.indexOf('\n') + 1, -3);
            return (
                <pre>
          <code class={`language-${language}`}>
            {Prism.highlight(code, Prism.languages[language] || Prism.languages.plain, language)}
          </code>
        </pre>
            );
        }

        return <div innerHTML={marked(content)} />;
    };

    return (
        <article class="media">
            <figure class="media-left">
                <p class="image is-48x48">
                    <img
                        src= {
                            props.message.sender === 'User'
                                ? '/user.png'
                                : props.message.sender === 'Compiler'
                                    ? '/Compiler.png'
                                    : '/KarlMarx.png'
                        }
                        alt="avatar"
                    />
                </p>
            </figure>
            <div class="media-content">
                <div class="content">
                    <p>
                        <strong>{props.message.sender}</strong>
                        <small class="ml-2">
                            {new Date(props.message.date || props.message.createdAt).toLocaleString()}
                        </small>
                        {(props.message.sender === 'User' || props.message.sender === 'Marx') && (
                            <button
                                class="button is-small is-ghost ml-2"
                                onClick={() => setIsEditing(true)}
                            >
                <span class="icon">
                  <i class="fas fa-edit"></i>
                </span>
                            </button>
                        )}
                        <br />
                        <Show
                            when={!isEditing()}
                            fallback={
                                <div class="field">
                                    <div class="control">
                    <textarea
                        class="textarea"
                        value={editContent()}
                        onInput={(e) => setEditContent(e.target.value)}
                    />
                                    </div>
                                    <div class="field is-grouped mt-2">
                                        <div class="control">
                                            <button
                                                class="button is-small is-primary"
                                                // onClick={() => {
                                                //     props.onEdit?.(editContent());
                                                //     setIsEditing(false);
                                                // }}
                                                onClick={handleEdit}
                                            >
                                                Sauvegarder
                                            </button>
                                        </div>
                                        <div class="control">
                                            <button
                                                class="button is-small"
                                                onClick={() => {
                                                    setEditContent(props.message.content);
                                                    setIsEditing(false);
                                                }}
                                            >
                                                Annuler
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            }
                        >
                            {renderMarkdown(props.message.content)}
                        </Show>
                    </p>
                </div>
            </div>
        </article>
    );
};