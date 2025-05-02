import {For} from "solid-js";

export const ChatToolbar = (props) => {
    return (
        <div class="level">
            <div class="level-left">
                <button
                    class="button is-ghost mr-4"
                    onClick={props.onBack}
                >
          <span class="icon">
            <i class="fas fa-arrow-left"></i>
          </span>
                    <span>Retour à l'historique</span>
                </button>
                <h1 class="title">
                    {props.title}
                </h1>
            </div>
            <div class="level-right">
                <div class="select mr-4">
                    <select
                        value={props.currentModel()}
                        onChange={(e) => props.onModelChange(e.target.value)}
                    >
                        <For each={props.availableModels()}>
                            {(model) => (
                                <option value={model}>
                                    {model.replace('_', ' ')}
                                </option>
                            )}
                        </For>
                    </select>
                </div>
            </div>
        </div>
    );
};