import React from "react";
import {array, string, bool, func} from "prop-types";
import uniqueId from "lodash/uniqueId";

/**
 * Utils offers a list of "connected" tools
 * through a "link" prop to connect to the app state
 */

export const Checkbox = ({checked, text, side, onChange}) => (
  <div>
    {side == "right"
      ? text
      : ""}
    <input
      type="checkbox"
      onChange={onChange}
      checked={checked}/> {side == "left"
      ? text
      : ""}
  </div>
);

Checkbox.propTypes = {
  checked: bool.isRequired,
  text: string.isRequired,
  side: string.isRequired,
  onChange: func.isRequired
};

export const Spaced = ({elements}) => (
  elements
    .map(x => <span key={uniqueId()}>{x}</span>)
    .reduce((prev, curr) => [
      prev,
      <span key = {uniqueId()} className = 'spacer-dot' />,
      curr
    ])
);

Spaced.propTypes = {
  elements: array.isRequired
};

// export const Select = ({link, opts, ...rest}) => (
//   <select
//     onChange={(e) => {
//       App.save(link, e.currentTarget.value);
//     }}
//     value={App.state[link]}
//     {...rest}>
//     {opts.map(opt =>
//       <option key={_.uid()}>{opt}</option>
//     )}
//   </select>
// );
//
// Select.propTypes = {
//   link: PropTypes.string,
//   opts: PropTypes.array
// };
//
// export const Textarea = ({link, ...rest}) => (
//   <textarea
//     onChange=
//       { (e) => { App.save("list", e.currentTarget.value); } }
//     value={App.state[link]}
//     {...rest} />
// );
//
// Textarea.propTypes = {
//   link: PropTypes.string
// };
