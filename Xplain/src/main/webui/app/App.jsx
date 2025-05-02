import { createSignal } from 'solid-js'
import { Router, Route } from '@solidjs/router';

import './App.css'
import ChatInterface from "../components/ChatInterface/ChatInterface";
import MessageList from "../components/MessageList/MessageList";


const App = () => {
    return (
      <Router>
        <Route path="/" component={MessageList} />
        <Route path="/chat" component={ChatInterface} />
        <Route path="/chat/:id" component={ChatInterface} />
      </Router>
    );
};


export default App;

/*function App() {
  const [myCount, setCount] = createSignal(0)

  return (
    <>
      <div>
              <Route path="/dashboard" component={DashBoard} />

          <ChatInterface/>

      </div>
    </>
  )

}*/