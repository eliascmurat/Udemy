import Expenses from './components/Expenses';

function App() {
  const expenses = [
    { id: 'e1', title: 'Test #01', amount: 123.45, date: new Date() },
    { id: 'e2', title: 'Test #02', amount: 456.78, date: new Date() },
    { id: 'e3', title: 'Test #03', amount: 789.12, date: new Date() }
  ];

  return (
    <div>
      <h1>Hello, World!</h1>
      <Expenses expenses={ expenses }/>
    </div>
  );
}

export default App;
