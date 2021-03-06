import React from "react";
import {array, string, bool, func} from "prop-types";

/**
 * Utils offers a list of "connected" tools
 * through a "link" prop to connect to the app state
 */

/**
 * @param  {boolean} {checked
 * @param  {string} text
 * @param  {string} side can by either right or left
 * @param  {function} onChange}
 */
export const Checkbox = ({checked, text, side="left", onChange}) => (
    <div>
        {side === "right"
            ? text
            : ""}
        <input
            type="checkbox"
            onChange={onChange}
            checked={checked}/>
            {side === "left"
                ? text
                : ""}
    </div>
);

Checkbox.propTypes = {
  checked: bool.isRequired,
  text: string.isRequired,
  side: string,
  onChange: func.isRequired
};

export const Spaced = ({elements}) => (
  elements
    .map((x,key) => <span key={key}>{x}</span>)
    .reduce((prev, curr, index) => [
      prev,
      <span key={curr} className = 'spacer-dot' />,
      curr
    ])
);

Spaced.propTypes = {
  elements: array.isRequired
};

export const Select = ({value, onChange, opts, ...rest}) => (
    <select
        onChange={onChange}
        value={value}
        {...rest}>
            {opts.map((opt, key) => <option key={key}>{opt}</option>)}
    </select>
);

Select.propTypes = {
    opts: array.isRequired,
    onChange: func.isRequired,
    value: string.isRequired
};

export const Textarea = ({value, onChange, ...rest}) => (
    <textarea
        onChange={onChange}
        value={value}
        {...rest} />
);

Textarea.propTypes = {
    value: string.isRequired,
    onChange: func.isRequired
};
