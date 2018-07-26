// const DownloadPanel = () => (
//     <fieldset className='download-controls fieldset'>
//         <legend className='legend game-legend'>Download</legend>
//         <Download/>
//         <Copy/>
//         <Log/>
//     </fieldset>
// );
//
// const Download = () => {
//     const filetypes = ["cod", "json", "mwdeck", "txt"];
//     const select = <Select link='filetype' opts={filetypes}/>;
//
//     return (
//         <div className='connected-container'>
//             <button className='connected-component' onClick={App._emit("download")}>
//                 Download as
//             </button>
//             <input
//                 type='text'
//                 className='download-filename connected-component'
//                 placeholder='filename'
//                 value={App.state["filename"]}
//                 onChange={e => {
//                     App.save("filename", e.currentTarget.checked);
//                 }}/>
//             {select}
//             <span className='download-button'/>
//         </div>
//     );
// };
//
// const Copy = () => (
//     <div className='copy-controls connected-container'>
//         <button
//             className='connected-component'
//             onClick={App._emit("copy")}>
//             Copy deck to clipboard
//         </button>
//     </div>
// );
//
// const Log = () => (
//     /draft|chaos/.test(App.state.type)
//         ? <div>
//             <button className='connected-component'
//                     onClick={App._emit("getLog")}>
//                 Download Draft Log
//             </button>
//         </div>
//         : <div/>
// );
//
// export default DownloadPanel;
